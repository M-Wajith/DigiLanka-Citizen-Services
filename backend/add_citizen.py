from flask import Blueprint, request, jsonify
from werkzeug.utils import secure_filename
from flask_cors import CORS
import os
import sqlite3

# Create a blueprint for citizen-related routes
citizen_bp = Blueprint('citizen', __name__)

# Enable CORS for the blueprint
CORS(citizen_bp, supports_credentials=True, origins='http://localhost:3000')

# Function to connect to the SQLite database
def get_db_connection():
    conn = sqlite3.connect('E:/Wajith/Horizon/Sem 8/project/Database/digilanka.db')
    conn.row_factory = sqlite3.Row
    return conn

@citizen_bp.route('/api/citizen', methods=['POST'])
def add_citizen():
    # Handle file uploads for both front and back images of the birth certificate
    birth_certificate_front = request.files.get('birthCertificateFront')
    birth_certificate_back = request.files.get('birthCertificateBack')

    print("Form data:", request.form)
    print("Files:", request.files)

    # Validate incoming data
    if not birth_certificate_front or not birth_certificate_back:
        return jsonify({'error': 'Both birth certificate front and back images are required.'}), 400

    required_fields = [
        'firstName', 'lastName', 'dateOfBirth', 'gender', 
        'nicNumber', 'birthCertificateNumber', 'permanentAddress', 'capturedFaceImage'
    ]

    for field in required_fields:
        if field not in request.form:
            return jsonify({'error': f'Missing required field: {field}'}), 400

    # Secure filenames (not stored on disk, but good practice)
    front_filename = secure_filename(birth_certificate_front.filename)
    back_filename = secure_filename(birth_certificate_back.filename)

    # Convert uploaded files to binary data (BLOBs) to store in SQLite
    try:
        birth_certificate_front_blob = birth_certificate_front.read()
        birth_certificate_back_blob = birth_certificate_back.read()
    except Exception as e:
        return jsonify({'error': f'Error reading files: {str(e)}'}), 500

    # Save the citizen's data to the database
    try:
        with get_db_connection() as conn:
            cursor = conn.cursor()
            cursor.execute('''
                INSERT INTO citizens (
                    first_name, middle_name, last_name, date_of_birth, gender, 
                    nic_number, birth_certificate_number, permanent_address, 
                    captured_face_image, birth_certificate_front, birth_certificate_back
                ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            ''', (
                request.form['firstName'],
                request.form.get('middleName', ''),  # Middle name is optional
                request.form['lastName'],
                request.form['dateOfBirth'],
                request.form['gender'],
                request.form['nicNumber'],
                request.form['birthCertificateNumber'],
                request.form['permanentAddress'],
                request.form['capturedFaceImage'],
                birth_certificate_front_blob,  # Save front image as BLOB
                birth_certificate_back_blob   # Save back image as BLOB
            ))

            conn.commit()

        return jsonify({'message': 'Citizen added successfully!'}), 201

    except sqlite3.IntegrityError as e:
        print(f"Integrity error: {str(e)}")  # Log integrity errors
        return jsonify({'error': f'Database integrity error: {str(e)}'}), 400

    except Exception as e:
        print(f"Unexpected error: {str(e)}")  # Log any unexpected errors
        return jsonify({'error': f'Unexpected error: {str(e)}'}), 500


@citizen_bp.route('/api/citizen/<nic_number>', methods=['GET'])
def get_citizen(nic_number):
    conn = get_db_connection()
    citizen = conn.execute('SELECT first_name, last_name, date_of_birth FROM citizens WHERE nic_number = ?', (nic_number,)).fetchone()
    conn.close()

    if citizen:
        return jsonify({
            "first_name": citizen["first_name"],
            "last_name": citizen["last_name"],
            "date_of_birth": citizen["date_of_birth"]
        })
    else:
        return jsonify({"error": "Citizen not found"}), 404
