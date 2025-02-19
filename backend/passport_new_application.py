from flask import Blueprint, request, jsonify
import sqlite3
import base64

passport_app = Blueprint('passport_app', __name__)

def encode_image(blob_data):
    """Encode binary image data to a Base64 string."""
    try:
        return base64.b64encode(blob_data).decode('utf-8') if blob_data else None
    except Exception as e:
        print(f"Error encoding image: {str(e)}")
        return None

@passport_app.route('/applications', methods=['GET'])
def get_applications():
    conn = sqlite3.connect('E:/Wajith/Horizon/Sem 8/project/Database/digilanka.db')
    cursor = conn.cursor()

    # Fetching data from the citizen_passport_application table
    cursor.execute("""
        SELECT travel_document, surname, other_name, nic_number,
               permanent_address, district, date_of_birth, birth_certificate_number, 
               gender, profession, dual_citizenship_number, phone_number, email_address, 
               nic_front_image, nic_back_image, birth_certificate_front, 
               birth_certificate_back, photo_receipt_image
        FROM citizen_passport_application
    """)
    
    rows = cursor.fetchall()
    conn.close()

    applications = []
    for row in rows:
        applications.append({
            'travelDocument': row[0],
            'surname': row[1],
            'otherName': row[2],
            'nicNumber': row[3],
            'permanentAddress': row[4],
            'district': row[5],
            'dateOfBirth': row[6],
            'birthCertificateNo': row[7],
            'gender': row[8],
            'profession': row[9],
            'dualCitizenshipNumber': row[10],
            'phoneNumber': row[11],
            'emailAddress': row[12],
            # Convert BLOBs to base64 strings for frontend use
            'nicFrontImage': encode_image(row[13]),
            'nicBackImage': encode_image(row[14]),
            'birthCertificateFront': encode_image(row[15]),
            'birthCertificateBack': encode_image(row[16]),
            'photoReceiptImage': encode_image(row[17])
        })

    return jsonify(applications)
@passport_app.route('/verify/<nic_number>', methods=['POST'])
def verify_application(nic_number):
    conn = sqlite3.connect('E:/Wajith/Horizon/Sem 8/project/Database/digilanka.db')
    cursor = conn.cursor()

    # Fetching the application details for the given NIC number
    cursor.execute("""
        SELECT birth_certificate_number, surname, other_name, nic_number
        FROM citizen_passport_application WHERE nic_number = ?
    """, (nic_number,))
    
    row = cursor.fetchone()

    if row:
        # Move data to verified_passport table
        cursor.execute("""
            INSERT INTO verified_passport (birth_certificate_number, surname, other_name, nic_number)
            VALUES (?, ?, ?, ?)
        """, (row[0], row[1], row[2], row[3]))

        # Delete the application from citizen_passport_application
        cursor.execute("""
            DELETE FROM citizen_passport_application WHERE nic_number = ?
        """, (nic_number,))

        # Update the status in the passport_status table
        cursor.execute("""
            INSERT OR REPLACE INTO passport_status (nic_number, status)
            VALUES (?, ?)
        """, (nic_number, "Verified"))
        
        conn.commit()

        response = {
            "message": "Application verified successfully.",
            "verifiedData": {
                "birthCertificateNumber": row[0],
                "surname": row[1],
                "otherName": row[2],
                "nicNumber": row[3]
            }
        }
    else:
        response = {"error": "Application not found for the provided NIC number."}

    conn.close()
    return jsonify(response)
# Database connection function
def get_db_connection():
    return sqlite3.connect('E:/Wajith/Horizon/Sem 8/project/Database/digilanka.db')

@passport_app.route('/application/status/applied/<nic_number>', methods=['GET'])
def check_application_exists(nic_number):
    conn = get_db_connection()
    cursor = conn.cursor()

    cursor.execute("SELECT status FROM passport_status WHERE nic_number = ?", (nic_number,))
    row = cursor.fetchone()
    conn.close()

    if row and row[0] in ["Applied", "Verified", "printed", "dispatched"]:
        return jsonify({"exists": True})
    else:
        return jsonify({"exists": False}), 404

@passport_app.route('/application/status/verified/<nic_number>', methods=['GET'])
def get_verified_status(nic_number):
    conn = get_db_connection()
    cursor = conn.cursor()

    # Check the status in the passport_status table
    cursor.execute("SELECT status FROM passport_status WHERE nic_number = ?", (nic_number,))
    row = cursor.fetchone()
    conn.close()

    # If the status is 'Verified', 'printed', or 'dispatched'
    if row and row[0] in ["Verified", "printed", "dispatched"]:
        return jsonify({"exists": True})
    else:
        return jsonify({"exists": False}), 404

@passport_app.route('/application/status/printed/<nic_number>', methods=['GET'])
def get_printed_status(nic_number):
    conn = get_db_connection()
    cursor = conn.cursor()

    # Check if NIC exists in printed_passport table
    cursor.execute("SELECT status FROM passport_status WHERE nic_number = ?", (nic_number,))
    row = cursor.fetchone()
    conn.close()

    # If status is 'printed' or 'dispatched'
    if row and row[0] in ["printed", "dispatched"]:
        return jsonify({"exists": True})
    else:
        return jsonify({"exists": False}), 404

@passport_app.route('/application/status/dispatched/<nic_number>', methods=['GET'])
def get_dispatched_status(nic_number):
    conn = get_db_connection()
    cursor = conn.cursor()

    # Check if NIC exists in dispatched_passport table
    cursor.execute("SELECT status FROM passport_status WHERE nic_number = ?", (nic_number,))
    row = cursor.fetchone()
    conn.close()

    # If status is 'dispatched'
    if row and row[0] == "dispatched":
        return jsonify({"exists": True})
    else:
        return jsonify({"exists": False}), 404