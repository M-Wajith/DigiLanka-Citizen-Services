from flask import Blueprint, request, jsonify, send_file
import sqlite3
import base64
import io

edit_citizen_bp = Blueprint('edit_citizen', __name__)

def get_db_connection():
    conn = sqlite3.connect('E:/Wajith/Horizon/Sem 8/project/Database/digilanka.db')
    conn.row_factory = sqlite3.Row
    return conn

@edit_citizen_bp.route('/get_citizen/<birth_certificate_number>', methods=['GET'])
def get_citizen(birth_certificate_number):
    print(f"Received birth_certificate_number: {birth_certificate_number}")  # Debugging

    # Convert to int if necessary
    try:
        birth_certificate_number = int(birth_certificate_number)
    except ValueError:
        return jsonify({'error': 'Invalid birth certificate number'}), 400

    conn = get_db_connection()
    citizen = conn.execute("SELECT * FROM citizens WHERE birth_certificate_number = ?", 
                           (birth_certificate_number,)).fetchone()
    conn.close()

    if not citizen:
        return jsonify({'error': 'Citizen not found'}), 404

    citizen_data = {
        'first_name': citizen['first_name'],
        'middle_name': citizen['middle_name'],
        'last_name': citizen['last_name'],
        'date_of_birth': citizen['date_of_birth'],
        'gender': citizen['gender'],
        'nic_number': citizen['nic_number'],
        'birth_certificate_number': citizen['birth_certificate_number'],
        'permanent_address': citizen['permanent_address']
    }
    return jsonify(citizen_data)


@edit_citizen_bp.route('/get_citizen_image/<image_type>', methods=['GET'])
def get_citizen_image(image_type):
    birth_certificate_number = request.args.get('birth_certificate_number')
    if not birth_certificate_number:
        return jsonify({'error': 'Birth certificate number is required'}), 400

    if image_type not in ['captured_face_image', 'birth_certificate_front', 'birth_certificate_back']:
        return jsonify({'error': 'Invalid image type'}), 400

    conn = get_db_connection()
    citizen = conn.execute(f"SELECT {image_type} FROM citizens WHERE birth_certificate_number = ?", 
                           (birth_certificate_number,)).fetchone()
    conn.close()

    if not citizen or not citizen[image_type]:
        return jsonify({'error': 'Image not found'}), 404

    return send_file(io.BytesIO(citizen[image_type]), mimetype='image/jpeg')


@edit_citizen_bp.route('/upload_citizen_image', methods=['POST'])
def upload_citizen_image():
    if 'image' not in request.files:
        return jsonify({'error': 'No image file provided'}), 400

    image_file = request.files['image']
    birth_certificate_number = request.form.get('birth_certificate_number')

    if not birth_certificate_number:
        return jsonify({'error': 'Birth certificate number is required'}), 400

    if image_file and image_file.filename.endswith(('jpg', 'jpeg', 'png')):
        image_data = image_file.read()
        
        # Update the citizen record with the new image
        conn = get_db_connection()
        conn.execute("UPDATE citizens SET captured_face_image = ? WHERE birth_certificate_number = ?",
                     (sqlite3.Binary(image_data), birth_certificate_number))
        conn.commit()
        conn.close()

        return jsonify({'message': 'Image uploaded successfully'}), 200
    return jsonify({'error': 'Invalid image format'}), 400

@edit_citizen_bp.route('/update_citizen', methods=['POST'])
def update_citizen():
    try:
        birth_certificate_number = request.form.get('birth_certificate_number')
        first_name = request.form.get('first_name')
        middle_name = request.form.get('middle_name', '')
        last_name = request.form.get('last_name')
        date_of_birth = request.form.get('date_of_birth')
        gender = request.form.get('gender').title()  # Ensure correct case format
        nic_number = request.form.get('nic_number')
        permanent_address = request.form.get('permanent_address')
        captured_face_image_base64 = request.form.get('captured_face_image')

        birth_certificate_front = request.files.get('birth_certificate_front')
        birth_certificate_back = request.files.get('birth_certificate_back')

        # Debug: Print received data
        print(f"Received Data: {birth_certificate_number}, {first_name}, {last_name}, {date_of_birth}, {gender}, {nic_number}, {permanent_address}")
        print(f"Captured Face Image Length: {len(captured_face_image_base64) if captured_face_image_base64 else 'None'}")
        print(f"Birth Certificate Front: {'Received' if birth_certificate_front else 'None'}")
        print(f"Birth Certificate Back: {'Received' if birth_certificate_back else 'None'}")

        if not all([birth_certificate_number, first_name, last_name, date_of_birth, gender, nic_number, permanent_address, captured_face_image_base64]):
            return jsonify({'error': 'Missing required fields'}), 400

        conn = get_db_connection()
        cursor = conn.cursor()

        cursor.execute("""
            UPDATE citizens 
            SET first_name = ?, middle_name = ?, last_name = ?, date_of_birth = ?, gender = ?, nic_number = ?, permanent_address = ? 
            WHERE birth_certificate_number = ?
        """, (first_name, middle_name, last_name, date_of_birth, gender, nic_number, permanent_address, birth_certificate_number))

        if captured_face_image_base64:
            cursor.execute("""
                UPDATE citizens 
                SET captured_face_image = ? 
                WHERE birth_certificate_number = ?
            """, (captured_face_image_base64, birth_certificate_number))

        if birth_certificate_front:
            cursor.execute("""
                UPDATE citizens 
                SET birth_certificate_front = ? 
                WHERE birth_certificate_number = ?
            """, (sqlite3.Binary(birth_certificate_front.read()), birth_certificate_number))

        if birth_certificate_back:
            cursor.execute("""
                UPDATE citizens 
                SET birth_certificate_back = ? 
                WHERE birth_certificate_number = ?
            """, (sqlite3.Binary(birth_certificate_back.read()), birth_certificate_number))

        conn.commit()
        return jsonify({'message': 'Citizen data updated successfully'}), 200

    except sqlite3.IntegrityError as e:
        print("IntegrityError:", str(e))
        return jsonify({'error': 'NIC number already exists'}), 400
    except Exception as e:
        print("Exception:", str(e))
        return jsonify({'error': str(e)}), 500
    finally:
        conn.close()
