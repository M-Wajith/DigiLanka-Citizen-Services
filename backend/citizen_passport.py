import sqlite3
import base64
from flask import Blueprint, request, jsonify

# Create a blueprint citizen_passport_bp
citizen_passport_bp = Blueprint('citizen_passport', __name__)

def decode_image(base64_image):
    """Decode the Base64 string and return binary data."""
    try:
        return base64.b64decode(base64_image)
    except Exception as e:
        print(f"Error decoding image: {str(e)}")
        return None

@citizen_passport_bp.route('/passportapply', methods=['POST'])
def submit_application():
    data = request.get_json()

    # Validate required fields
    required_fields = [
        'travelDocument', 'surname', 'otherName', 'nicNumber', 'permanentAddress', 'district', 'dateOfBirth', 'birthCertificateNumber', 'gender', 'profession', 'phoneNumber', 'emailAddress',
        'nicFrontImage', 'nicBackImage', 'birthCertificateFront', 'birthCertificateBack', 'photoReceiptImage'
    ]

    # Check for missing fields
    for field in required_fields:
        if field not in data:
            return jsonify({"error": f"{field} is required"}), 400

    # Optional field
    dual_citizenship_number = data.get('dualCitizenshipNumber')  # None if not provided

    # Prepare data for insertion
    relevant_data = {
        'travel_document': data['travelDocument'], 'surname': data['surname'],
        'other_name': data['otherName'], 'nic_number': data['nicNumber'], 'permanent_address': data['permanentAddress'], 'district': data['district'], 'date_of_birth': data['dateOfBirth'],
        'birth_certificate_number': data['birthCertificateNumber'], 'gender': data['gender'],
        'profession': data['profession'], 'dual_citizenship_number': dual_citizenship_number, 'phone_number': data['phoneNumber'], 'email_address': data['emailAddress']
    }

    # Process images (decode base64 and store as binary)
    images = {
        'nic_front_image': decode_image(data['nicFrontImage']), 'nic_back_image': decode_image(data['nicBackImage']),
        'birth_certificate_front': decode_image(data['birthCertificateFront']), 'birth_certificate_back': decode_image(data['birthCertificateBack']),
        'photo_receipt_image': decode_image(data['photoReceiptImage'])
    }

    # Ensure all images are decoded properly
    for field, image in images.items():
        if not image:
            return jsonify({"error": f"Error processing {field}"}), 500

    # Connect to the database and insert the data
    try:
        with sqlite3.connect('E:/Wajith/Horizon/Sem 8/project/Database/digilanka.db') as conn:
            cursor = conn.cursor()
            cursor.execute('''
                INSERT INTO citizen_passport_application (
                    travel_document, surname, other_name, nic_number, permanent_address, district,
                    date_of_birth, birth_certificate_number, gender, profession, dual_citizenship_number,
                    phone_number, email_address, nic_front_image, nic_back_image,
                    birth_certificate_front, birth_certificate_back, photo_receipt_image
                ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            ''', (
                relevant_data['travel_document'], relevant_data['surname'], relevant_data['other_name'],
                relevant_data['nic_number'], relevant_data['permanent_address'], relevant_data['district'], relevant_data['date_of_birth'], relevant_data['birth_certificate_number'], relevant_data['gender'], relevant_data['profession'],
                relevant_data['dual_citizenship_number'], relevant_data['phone_number'], relevant_data['email_address'],
                images['nic_front_image'], images['nic_back_image'], images['birth_certificate_front'], images['birth_certificate_back'], images['photo_receipt_image']
            ))

            # Insert initial status into passport_status table
            cursor.execute('''
                INSERT INTO passport_status (nic_number, status) VALUES (?, ?)
            ''', (relevant_data['nic_number'], 'Applied'))  # Set initial status to 'Applied'

            conn.commit()
        return jsonify({"message": "Application submitted successfully"}), 201
    except sqlite3.Error as e:
        print("SQLite error:", str(e))
        return jsonify({"error": "Database error occurred"}), 500
    except Exception as e:
        print("Unexpected error:", str(e))
        return jsonify({"error": "An unexpected error occurred"}), 500
