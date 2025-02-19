import sqlite3
import base64
import logging
from flask import Blueprint, request, jsonify

# Define Blueprint
citizen_nic_bp = Blueprint('citizen_nic', __name__)

# Function to decode Base64 images (Ensure this function exists in your code)
def decode_image(base64_string):
    if base64_string:
        try:
            return base64.b64decode(base64_string)
        except Exception:
            return None
    return None

@citizen_nic_bp.route('/nicapply', methods=['POST'])
def submit_application():
    data = request.get_json()

    # Log incoming data for debugging
    logging.debug(f"Incoming data: {data}")

    # Validate required fields
    required_fields = [
        'name', 'surname', 'gender', 'dateOfBirth', 'civilStatus', 'profession',
        'birthPlace', 'district', 'email', 'phoneNumber', 'oldNicNumber',
        'permanentAddress', 'policeComplaint', 'photoReceipt',
        'birthCertFront', 'birthCertBack'
    ]

    # Check for missing fields
    for field in required_fields:
        if field not in data:
            logging.warning(f"Missing field: {field}")
            return jsonify({"error": f"{field} is required"}), 400

    # Prepare data for insertion
    relevant_data = {
        'name': data['name'],
        'surname': data['surname'],
        'gender': data['gender'],
        'date_of_birth': data['dateOfBirth'],
        'civil_status': data['civilStatus'],
        'profession': data['profession'],
        'birth_place': data['birthPlace'],
        'district': data['district'],
        'email': data['email'],
        'phone_number': data['phoneNumber'],
        'old_nic_number': data['oldNicNumber'],
        'permanent_address': data['permanentAddress']
    }

    # Log the value of permanent_address
    logging.debug(f"Permanent Address: {relevant_data['permanent_address']}")

    # Decode images from Base64
    images = {
        'police_complaint': decode_image(data.get('policeComplaint')),
        'photo_receipt': decode_image(data.get('photoReceipt')),
        'birth_certificate_front': decode_image(data.get('birthCertFront')),
        'birth_certificate_back': decode_image(data.get('birthCertBack')),
    }

    # Check if any image decoding failed
    for key, value in images.items():
        if value is None:
            logging.warning(f"Failed to decode image: {key}")
            return jsonify({"error": f"Invalid image data for {key}"}), 400

    # Connect to the database and insert the data
    try:
        with sqlite3.connect('E:/Wajith/Horizon/Sem 8/project/Database/digilanka.db') as conn:
            cursor = conn.cursor()
            cursor.execute('''
                INSERT INTO citizen_nic_application (
                    name, surname, gender, date_of_birth, civil_status, profession,
                    birth_place, district, permanent_address, police_complaint,
                    photo_receipt, birth_certficate_front, birth_certficate_back,
                    email, phone_number, old_nic_number
                ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            ''', (
                relevant_data['name'], relevant_data['surname'], relevant_data['gender'],
                relevant_data['date_of_birth'], relevant_data['civil_status'],
                relevant_data['profession'], relevant_data['birth_place'],
                relevant_data['district'], relevant_data['permanent_address'],
                images['police_complaint'], images['photo_receipt'],
                images['birth_certificate_front'], images['birth_certificate_back'],
                relevant_data['email'], relevant_data['phone_number'],
                relevant_data['old_nic_number']
            ))

            # Update the NIC status
            old_nic_number = relevant_data['old_nic_number']
            cursor.execute("""
                INSERT OR REPLACE INTO nic_status (old_nic_number, status)
                VALUES (?, ?)
            """, (old_nic_number, "applied"))

            conn.commit()
            logging.info("NIC application submitted successfully")

        return jsonify({"message": "NIC application submitted successfully"}), 201

    except sqlite3.Error as e:
        logging.error("SQLite error: %s", str(e))
        return jsonify({"error": "Database error occurred"}), 500
    except Exception as e:
        logging.error("Unexpected error: %s", str(e))
        return jsonify({"error": "An unexpected error occurred"}), 500
@citizen_nic_bp.route('/nicapplications', methods=['GET'])
def get_all_applications():
    try:
        with sqlite3.connect('E:/Wajith/Horizon/Sem 8/project/Database/digilanka.db') as conn:
            cursor = conn.cursor()
            cursor.execute('SELECT name, date_of_birth, permanent_address, old_nic_number FROM citizen_nic_application')
            applications = cursor.fetchall()

            return jsonify([
                {
                    "name": app[0],
                    "date_of_birth": app[1],
                    "permanent_address": app[2],
                    "old_nic_number": app[3]
                } for app in applications
            ]), 200

    except sqlite3.Error as e:
        logging.error(f"SQLite error: {e}")
        return jsonify({"error": "Database error occurred"}), 500


def encode_image(image_data):
    """Utility function to encode BLOB image data to a base64 string."""
    return base64.b64encode(image_data).decode('utf-8') if image_data else None


@citizen_nic_bp.route('/application/<old_nic_number>', methods=['GET'])
def get_citizen_nic_application(old_nic_number):
    try:
        with sqlite3.connect('E:/Wajith/Horizon/Sem 8/project/Database/digilanka.db') as conn:
            cursor = conn.cursor()

            # Fetching the specific application by Old NIC number
            cursor.execute("""
                    SELECT name, surname, gender, date_of_birth, civil_status, profession, birth_place, 
                        district, permanent_address, police_complaint, photo_receipt, 
                        birth_certficate_front, birth_certficate_back, email, phone_number, old_nic_number
                    FROM citizen_nic_application WHERE old_nic_number = ?
                """, (old_nic_number,))
            row = cursor.fetchone()

            if row:
                application = {
                    'name': row[0], 'surname': row[1], 'gender': row[2], 'date_of_birth': row[3],
                    'civil_status': row[4], 'profession': row[5], 'birth_place': row[6], 'district': row[7],
                    'permanent_address': row[8], 'police_complaint': encode_image(row[9]),
                    'photo_receipt': encode_image(row[10]), 'birth_certificate_front': encode_image(row[11]),
                    'birth_certificate_back': encode_image(row[12]), 'email': row[13],
                    'phone_number': row[14], 'old_nic_number': row[15]
                }
                return jsonify(application), 200
            else:
                return jsonify({"error": "Application not found"}), 404

    except sqlite3.Error as e:
        logging.error(f"SQLite error: {e}")
        return jsonify({"error": "Database error occurred"}), 500
    
@citizen_nic_bp.route('/verify/<old_nic_number>', methods=['POST'])
def verify_nic_application(old_nic_number):
    DATABASE_PATH = 'E:/Wajith/Horizon/Sem 8/project/Database/digilanka.db'
    """Verify a NIC application and move it to the verified table."""
    try:
        with sqlite3.connect(DATABASE_PATH) as conn:
            cursor = conn.cursor()

            # Fetch application details
            cursor.execute("""
                SELECT name, old_nic_number, date_of_birth, permanent_address
                FROM citizen_nic_application WHERE old_nic_number = ?
            """, (old_nic_number,))
            row = cursor.fetchone()

            if row:
                # Insert into verified_nic table
                cursor.execute("""
                    INSERT INTO verified_nic (name, old_nic_number, date_of_birth, permanent_address)
                    VALUES (?, ?, ?, ?)
                """, (row[0], row[1], row[2], row[3]))

                # Delete from citizen_nic_application
                cursor.execute("""
                    DELETE FROM citizen_nic_application WHERE old_nic_number = ?
                """, (old_nic_number,))

                # Update NIC status
                cursor.execute("""
                    INSERT OR REPLACE INTO nic_status (old_nic_number, status)
                    VALUES (?, ?)
                """, (old_nic_number, "Verified"))

                conn.commit()

                response = {
                    "message": "Application verified successfully.",
                    "verifiedData": {
                        "name": row[0], "old_nic_number": row[1], "date_of_birth": row[2], "permanent_address": row[3]
                    }
                }
                return jsonify(response), 200
            else:
                return jsonify({"error": "Application not found for the provided Old NIC number."}), 404

    except sqlite3.Error as e:
        logging.error("SQLite error: %s", str(e))
        return jsonify({"error": "Database error occurred"}), 500
    
@citizen_nic_bp.route('/application/status/applied/<nic_number>', methods=['GET'])
def check_application_exists(nic_number):
    conn = sqlite3.connect('E:/Wajith/Horizon/Sem 8/project/Database/digilanka.db')
    cursor = conn.cursor()

    cursor.execute("SELECT status FROM nic_status WHERE old_nic_number = ?", (nic_number,))
    row = cursor.fetchone()
    conn.close()

    if row and (row[0] in ["applied", "Verified", "printed", "dispatched"]):
        return jsonify({"exists": True})
    else:
        return jsonify({"exists": False}), 404


@citizen_nic_bp.route('/application/status/verified/<nic_number>', methods=['GET'])
def get_verified_status(nic_number):
    conn = sqlite3.connect('E:/Wajith/Horizon/Sem 8/project/Database/digilanka.db')
    cursor = conn.cursor()

    cursor.execute("SELECT status FROM nic_status WHERE old_nic_number = ?", (nic_number,))
    row = cursor.fetchone()
    conn.close()

    # If the status is 'Verified'
    if row and (row[0] in ["Verified", "printed", "dispatched"]):
        return jsonify({"exists": True})
    else:
        return jsonify({"exists": False}), 404


@citizen_nic_bp.route('/application/status/printed/<nic_number>', methods=['GET'])
def get_printed_status(nic_number):
    conn = sqlite3.connect('E:/Wajith/Horizon/Sem 8/project/Database/digilanka.db')
    cursor = conn.cursor()

    cursor.execute("SELECT status FROM nic_status WHERE old_nic_number = ?", (nic_number,))
    row = cursor.fetchone()
    conn.close()

    # If the status is 'printed' or 'dispatched'
    if row and (row[0] in ["printed", "dispatched"]):
        return jsonify({"exists": True})
    else:
        return jsonify({"exists": False}), 404


@citizen_nic_bp.route('/application/status/dispatched/<nic_number>', methods=['GET'])
def get_dispatched_status(nic_number):
    conn = sqlite3.connect('E:/Wajith/Horizon/Sem 8/project/Database/digilanka.db')
    cursor = conn.cursor()

    cursor.execute("SELECT status FROM nic_status WHERE old_nic_number = ?", (nic_number,))
    row = cursor.fetchone()
    conn.close()

    # If the status is 'dispatched'
    if row and (row[0] in ["dispatched"]):
        return jsonify({"exists": True})
    else:
        return jsonify({"exists": False}), 404