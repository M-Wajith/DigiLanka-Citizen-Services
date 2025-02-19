import sqlite3
from flask import Blueprint, request, jsonify

# Define the blueprint
verified_passport_blueprint = Blueprint('verified_passport', __name__)

# Database connection function

def get_db_connection():
    return sqlite3.connect('E:/Wajith/Horizon/Sem 8/project/Database/digilanka.db')

@verified_passport_blueprint.route('/verified_passport', methods=['GET'])
def verified_passport_all():
    conn = sqlite3.connect('E:/Wajith/Horizon/Sem 8/project/Database/digilanka.db')
    conn.row_factory = sqlite3.Row  # ✅ Enables dictionary-like row access
    cursor = conn.cursor()

    cursor.execute("SELECT * FROM verified_passport")
    data = cursor.fetchall()
    
    conn.close()

    if not data:
        return jsonify({"error": "No records found"}), 404

    # ✅ Convert rows to dictionaries with expected frontend keys
    result = [
        {
            "surname": row["surname"],
            "otherName": row["other_name"],  # ✅ Rename for React
            "nicNumber": row["nic_number"],  # ✅ Rename for React
            "birthCertificateNumber": row["birth_certificate_number"],  # ✅ Rename for React
        }
        for row in data
    ]

    return jsonify(result), 200

@verified_passport_blueprint.route('/verified_passport/print/<nic_number>', methods=['POST'])
def move_to_printed_passport(nic_number):
    conn = get_db_connection()
    cursor = conn.cursor()

    try:
        # Retrieve the data from verified_passport
        cursor.execute("SELECT birth_certificate_number, surname, other_name, nic_number FROM verified_passport WHERE nic_number = ?", (nic_number,))
        row = cursor.fetchone()

        if row:
            # Insert the data into printed_passport
            cursor.execute("""
                INSERT INTO printed_passport (birth_certificate_number, surname, other_name, nic_number)
                VALUES (?, ?, ?, ?)
            """, (row[0], row[1], row[2], row[3]))

            # Update the status in passport_status
            cursor.execute("""
                INSERT OR REPLACE INTO passport_status (nic_number, status)
                VALUES (?, ?)
            """, (nic_number, "printed"))

            # Delete the data from verified_passport
            cursor.execute("DELETE FROM verified_passport WHERE nic_number = ?", (nic_number,))
            conn.commit()

            return jsonify({"message": "Application moved to printed_passport"}), 200
        
    except Exception as e:
        conn.rollback()
        return jsonify({"error": str(e)}), 500
    finally:
        conn.close()


@verified_passport_blueprint.route('/dispatched_passport', methods=['GET'])
def dispatched_passport_all():
    conn = sqlite3.connect('E:/Wajith/Horizon/Sem 8/project/Database/digilanka.db')
    conn.row_factory = sqlite3.Row  # ✅ Enables dictionary-like row access
    cursor = conn.cursor()

    cursor.execute("SELECT * FROM dispatched_passport")
    data = cursor.fetchall()
    
    conn.close()

    if not data:
        return jsonify({"error": "No records found"}), 404

    # ✅ Convert rows to dictionaries with expected frontend keys
    result = [
        {
            "surname": row["surname"],
            "otherName": row["other_name"],  # ✅ Rename for React
            "nicNumber": row["nic_number"],  # ✅ Rename for React
            "birthCertificateNumber": row["birth_certificate_number"],  # ✅ Rename for React
        }
        for row in data
    ]

    return jsonify(result), 200