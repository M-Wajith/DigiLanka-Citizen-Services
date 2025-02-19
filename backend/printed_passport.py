import sqlite3
from flask import Blueprint, request, jsonify
import logging

# Define the blueprint
printed_passport_blueprint = Blueprint('printed_passport', __name__)

# Database connection function
def get_db_connection():
    return sqlite3.connect('E:/Wajith/Horizon/Sem 8/project/Database/digilanka.db')

@printed_passport_blueprint.route('/printed_passport', methods=['GET'])
def verified_passport_all():
    conn = sqlite3.connect('E:/Wajith/Horizon/Sem 8/project/Database/digilanka.db')
    conn.row_factory = sqlite3.Row  # ✅ Enables dictionary-like row access
    cursor = conn.cursor()

    cursor.execute("SELECT * FROM printed_passport")
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

@printed_passport_blueprint.route('/printed_passport/dispatch/<nic_number>', methods=['POST'])
def move_to_dispatched_passport(nic_number):
    conn = get_db_connection()
    cursor = conn.cursor()

    try:
        # Retrieve the data from printed_passport
        cursor.execute("SELECT birth_certificate_number, surname, other_name, nic_number FROM printed_passport WHERE nic_number = ?", (nic_number,))
        row = cursor.fetchone()

        if row:
            # Insert the data into dispatched_passport
            cursor.execute("""
                INSERT INTO dispatched_passport (birth_certificate_number, surname, other_name, nic_number)
                VALUES (?, ?, ?, ?)
            """, (row[0], row[1], row[2], row[3]))

            # Update the status in passport_status
            cursor.execute("""
                INSERT OR REPLACE INTO passport_status (nic_number, status)
                VALUES (?, ?)
            """, (nic_number, "dispatched"))

            # Delete the data from printed_passport
            cursor.execute("DELETE FROM printed_passport WHERE nic_number = ?", (nic_number,))
            conn.commit()

            return jsonify({"message": "Application moved to dispatched_passport"}), 200
        else:
            return jsonify({"error": "Application not found"}), 404
    except Exception as e:
        conn.rollback()
        return jsonify({"error": str(e)}), 500
    finally:
        conn.close()

@printed_passport_blueprint.route('/printed_nic', methods=['GET'])
def get_all_printed_nic():
    """Fetch all printed NIC applications."""
    try:
        with get_db_connection() as conn:
            cursor = conn.cursor()

            # Fetch all verified NIC applications
            cursor.execute("""
                SELECT id, name, old_nic_number, date_of_birth, permanent_address 
                FROM printed_nic
            """)
            applications = cursor.fetchall()

            if not applications:
                return jsonify({"message": "No verified applications found"}), 404

            # Convert data into a list of dictionaries
            verified_list = [
                {
                    "id": row[0],
                    "name": row[1],
                    "old_nic_number": row[2],
                    "date_of_birth": row[3],
                    "permanent_address": row[4]
                }
                for row in applications
            ]

        return jsonify({"verified_applications": verified_list}), 200

    except sqlite3.Error as e:
        logging.error("SQLite error: %s", str(e))
        return jsonify({"error": "Database error occurred"}), 500

