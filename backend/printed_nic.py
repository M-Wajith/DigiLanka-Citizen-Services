from flask import Blueprint, jsonify
import sqlite3
import logging

# Set up logging
logging.basicConfig(level=logging.INFO)

# Define blueprint
printed_nic_bp = Blueprint('printed_nic', __name__)

# Database connection function
def get_db_connection():
    return sqlite3.connect('E:/Wajith/Horizon/Sem 8/project/Database/digilanka.db')

@printed_nic_bp.route('/dispatch_nic/<string:old_nic_number>', methods=['POST'])
def dispatch_nic(old_nic_number):
    """Move a printed NIC application to the dispatched NIC table."""
    try:
        with get_db_connection() as conn:
            cursor = conn.cursor()

            # Ensure the application exists in printed_nic
            cursor.execute("""
                SELECT name, old_nic_number, date_of_birth, permanent_address
                FROM printed_nic WHERE old_nic_number = ?
            """, (old_nic_number,))
            application = cursor.fetchone()

            if not application:
                return jsonify({"error": "Application not found"}), 404

            # Move application to dispatched_nic
            cursor.execute("""
                INSERT INTO dispatched_nic (name, old_nic_number, date_of_birth, permanent_address)
                VALUES (?, ?, ?, ?)
            """, (application[0], application[1], application[2], application[3]))

            # Update NIC status
            cursor.execute("""
                INSERT OR REPLACE INTO nic_status (old_nic_number, status)
                VALUES (?, ?)
            """, (old_nic_number, "dispatched"))

            # Delete from printed_nic after successful insertion
            cursor.execute("DELETE FROM printed_nic WHERE old_nic_number = ?", (old_nic_number,))

            conn.commit()

        return jsonify({"message": "Application dispatched successfully"}), 200

    except sqlite3.Error as e:
        logging.error("SQLite error: %s", str(e))
        return jsonify({"error": "Database error occurred"}), 500
    
@printed_nic_bp.route('/dispatch_nic', methods=['GET'])
def get_all_verified_nic():
    """Fetch all verified NIC applications."""
    try:
        with get_db_connection() as conn:
            cursor = conn.cursor()

            # Fetch all verified NIC applications
            cursor.execute("""
                SELECT id, name, old_nic_number, date_of_birth, permanent_address 
                FROM dispatched_nic
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

