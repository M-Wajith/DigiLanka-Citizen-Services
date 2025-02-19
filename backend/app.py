from flask import Flask, request, jsonify, session
from flask_cors import CORS
import sqlite3
import numpy as np
import io
import cv2
import os
import base64
import json
from PIL import Image
from deepface import DeepFace

# Import Blueprints
from add_citizen import citizen_bp
from edit_citizen import edit_citizen_bp

from citizen_passport import citizen_passport_bp
from passport_new_application import passport_app
from verified_passport import verified_passport_blueprint
from printed_passport import printed_passport_blueprint

from medical_appointment import medical_appointment
from written_appointment import written_appointment
from practical_appointment import practical_appointment
from citizen_nic import citizen_nic_bp
from verified_nic import verified_nic_bp
from printed_nic import printed_nic_bp


app = Flask(__name__)
CORS(app, supports_credentials=True, origins='http://localhost:3000')


# App Configurations
app.secret_key = os.environ.get('FLASK_SECRET_KEY', 'change_this')
app.config['UPLOAD_FOLDER'] = 'uploads'
app.config['SESSION_COOKIE_NAME'] = 'session'
app.config['SESSION_PERMANENT'] = False
app.config['SESSION_USE_SIGNER'] = True
app.config['SESSION_TYPE'] = 'filesystem'

# Register Blueprints
app.register_blueprint(citizen_bp)

app.register_blueprint(citizen_passport_bp, url_prefix='/api')
app.register_blueprint(passport_app, url_prefix='/passport')
app.register_blueprint(verified_passport_blueprint, url_prefix='/api')
app.register_blueprint(printed_passport_blueprint, url_prefix='/api')

app.register_blueprint(medical_appointment, url_prefix='/api')
app.register_blueprint(written_appointment)
app.register_blueprint(practical_appointment)
app.register_blueprint(citizen_nic_bp, url_prefix='/api')
app.register_blueprint(verified_nic_bp, url_prefix='/api')
app.register_blueprint(printed_nic_bp, url_prefix='/api')
app.register_blueprint(edit_citizen_bp)

print(app.url_map)

# Database Connection
def get_db_connection():
    conn = sqlite3.connect('E:/Wajith/Horizon/Sem 8/project/Database/digilanka.db')
    conn.row_factory = sqlite3.Row
    return conn  # Ensure the caller closes the connection


# Get Officer Role
def get_officer_role(user_id):
    with get_db_connection() as conn:
        divisional_officer = conn.execute('SELECT * FROM Divisional_officers WHERE user_id = ?', (user_id,)).fetchone()
        if divisional_officer:
            return 'divisional-dashboard'

        passport_officer = conn.execute('SELECT * FROM Passport_officers WHERE user_id = ?', (user_id,)).fetchone()
        if passport_officer:
            return 'passport-dashboard'

        nic_officer = conn.execute('SELECT * FROM NIC_officers WHERE user_id = ?', (user_id,)).fetchone()
        if nic_officer:
            return 'nic-dashboard'

        license_officer = conn.execute('SELECT * FROM License_officers WHERE user_id = ?', (user_id,)).fetchone()
        if license_officer:
            return 'license-dashboard'

    return None


# Login Route
@app.route('/login', methods=['POST'])
def login():
    # Check request data and ensure you are receiving JSON
    if not request.is_json:
        return jsonify({'error': 'Invalid input'}), 400

    data = request.json
    username = data.get('username')
    password = data.get('password')

    # Database interaction code (with error handling)
    try:
        conn = get_db_connection()
        user = conn.execute('SELECT * FROM Users WHERE username = ? AND password = ?', (username, password)).fetchone()
        
        if user:
            user_id = user['user_id']
            role = get_officer_role(user_id)
            
            if role:
                session['user_id'] = user_id
                return jsonify({'redirect': f'/{role}'}), 200

        return jsonify({'error': 'Invalid credentials or no role found'}), 401

    except Exception as e:
        print(f"Error: {e}")
        return jsonify({'error': 'Internal server error'}), 500

# Logout Route
@app.route('/logout', methods=['POST'])
def logout():
    session.clear()
    return jsonify({'message': 'Logged out successfully'})

# Protected Route Example
@app.route('/protected', methods=['GET'])
def protected():
    if 'user_id' not in session:
        return jsonify({'error': 'Unauthorized'}), 401
    return jsonify({'message': 'You have access to this protected route'})


@app.route('/check_nic', methods=['GET'])
def check_nic():
    nic_number = request.args.get('nic_number')

    conn = get_db_connection()
    citizen = conn.execute('SELECT * FROM citizens WHERE nic_number = ?', (nic_number,)).fetchone()
    conn.close()

    if citizen:
        session['nic_number'] = nic_number  # Store NIC number in session
        print("NIC Number stored in session:", session.get('nic_number'))
        return jsonify({'exists': True})
    else:
        return jsonify({'exists': False})


# Function to resize an image while maintaining aspect ratio
def resize_image(image, target_size):
    original_size = image.size
    aspect_ratio = original_size[0] / original_size[1]

    if aspect_ratio > 1:  # Image is wider
        new_width = target_size[0]
        new_height = int(target_size[0] / aspect_ratio)
    else:  # Image is taller or square
        new_height = target_size[1]
        new_width = int(target_size[1] * aspect_ratio)

    return image.resize((new_width, new_height), Image.LANCZOS)  # Use LANCZOS filter for high-quality downsampling


# Function to decode Base64 string into an image
def decode_base64_image(base64_string):
    try:
        # Remove metadata prefix if present
        if base64_string.startswith("data:image/jpeg;base64,"):
            base64_string = base64_string.replace("data:image/jpeg;base64,", "")

        # Add padding to make the length a multiple of 4
        base64_string += "=" * ((4 - len(base64_string) % 4) % 4)

        # Decode Base64 string to binary data
        img_data = base64.b64decode(base64_string)

        # Convert binary data to numpy array
        np_arr = np.frombuffer(img_data, np.uint8)

        # Decode numpy array to image
        img = cv2.imdecode(np_arr, cv2.IMREAD_COLOR)

        if img is None:
            raise ValueError("Image decoding failed; check the Base64 input.")

        return cv2.cvtColor(img, cv2.COLOR_BGR2RGB)  # Convert to RGB for DeepFace
    except Exception as e:
        print("Error decoding Base64 image:", e)
        return None


# Function to compare two face images
def compare_faces(base64_img1, base64_img2):
    img1 = decode_base64_image(base64_img1)
    img2 = decode_base64_image(base64_img2)

    if img1 is None or img2 is None:
        print("Image decoding failed. Ensure both Base64 strings are valid images.")
        return False

    try:
        # Perform face verification
        result = DeepFace.verify(img1, img2, model_name="Facenet")
        return result['verified']  # Returns True if they match
    except Exception as e:
        print("Error during face verification:", e)
        return False


@app.route('/check_face', methods=['POST'])
def check_face():
    print("Request data:", request.data)  # Log raw request data for debugging

    try:
        data = request.json

        if data is None:
            return jsonify({'error': 'Invalid JSON format or no data provided'}), 400

        captured_image_base64 = data.get('captured_face_image')
        nic_number = data.get('nic_number')  # Get NIC number from the request

        print("Captured image base64:", captured_image_base64)
        print("NIC Number from request:", nic_number)  # Log NIC number

        if captured_image_base64 is None or not isinstance(captured_image_base64, str):
            return jsonify({'error': 'Captured face image not provided or invalid.'}), 400

        if not nic_number:
            return jsonify({'error': 'NIC number not provided'}), 400

        # Retrieve the stored image from the database based on the NIC number
        conn = get_db_connection()
        stored_citizen = conn.execute(
            'SELECT captured_face_image FROM citizens WHERE nic_number = ?', (nic_number,)
        ).fetchone()
        conn.close()

        print("Stored citizen record:", stored_citizen)  # Log query result

        if stored_citizen is None:
            return jsonify({'error': 'No citizen found with the given NIC number'}), 404

        stored_image_base64 = stored_citizen['captured_face_image']

        # Verify both images
        if stored_image_base64 and captured_image_base64:
            is_verified = compare_faces(stored_image_base64, captured_image_base64)
            return jsonify({'match': is_verified}), 200
        else:
            return jsonify({'error': 'Please provide valid Base64 strings for both images.'}), 400

    except Exception as e:
        print(f'Error processing the face comparison: {str(e)}')
        return jsonify({'error': f'Error processing the face comparison: {str(e)}'}), 500

# Run the Flask App
if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000, debug=True)
