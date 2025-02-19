from flask import Blueprint, jsonify, request
from datetime import datetime
import sqlite3
import pytz

# Create a Blueprint for practical appointments
practical_appointment = Blueprint('practical_appointment', __name__)

def get_db_connection():
    conn = sqlite3.connect('E:/Wajith/Horizon/Sem 8/project/Database/digilanka.db', check_same_thread=False)
    conn.row_factory = sqlite3.Row
    return conn

@practical_appointment.route('/practical_appointments', methods=['POST'])
def create_practical_appointment():
    data = request.json
    start_time = data.get('start_time')  # Expected format: "2024-10-27T03:00:00.000Z"
    end_time = data.get('end_time')  # Expected format: "2024-10-27T03:30:00.000Z"
    nic = data.get('nic')  # Citizen's ID or name

    # Parse the incoming ISO 8601 formatted datetime strings into datetime objects
    start_datetime = datetime.fromisoformat(start_time[:-1])  # Removing the 'Z' at the end for fromisoformat
    end_datetime = datetime.fromisoformat(end_time[:-1])  # Removing the 'Z' at the end for fromisoformat

    # Set the timezone to UTC (the incoming time is in UTC)
    utc = pytz.utc
    start_datetime = utc.localize(start_datetime)
    end_datetime = utc.localize(end_datetime)

    # Format the datetime objects into the desired string format, but with IST-like offset in UTC
    formatted_start_time = start_datetime.strftime('%a %b %d %Y %H:%M:%S GMT+0530 (India Standard Time)')
    formatted_end_time = end_datetime.strftime('%a %b %d %Y %H:%M:%S GMT+0530 (India Standard Time)')

    # Using a context manager for database operations with get_db_connection() as conn:
    with get_db_connection() as conn:
        cursor = conn.cursor()
        cursor.execute(
            "INSERT INTO practical_calendar_slots (start_time, end_time, status, nic) VALUES (?, ?, 'requested', ?)",
            (formatted_start_time, formatted_end_time, nic)
        )
        conn.commit()

    return jsonify({'message': 'Practical appointment request created successfully'}), 201

@practical_appointment.route('/fetch_practical_appointments', methods=['GET'])
def get_practical_appointments():
    conn = get_db_connection()
    cursor = conn.cursor()
    cursor.execute("SELECT id, start_time, end_time, status, nic FROM practical_calendar_slots WHERE status != 'rejected'")
    slots = cursor.fetchall()
    conn.close()

    slots_list = [{'id': slot['id'], 'start_time': slot['start_time'], 'end_time': slot['end_time'], 'status': slot['status'], 'nic': slot['nic']} for slot in slots]
    return jsonify(slots_list)

@practical_appointment.route('/practical_appointments/<int:slot_id>/accept', methods=['POST'])
def accept_practical_appointment(slot_id):
    conn = get_db_connection()
    cursor = conn.cursor()
    cursor.execute("UPDATE practical_calendar_slots SET status = 'booked' WHERE id = ?", (slot_id,))
    conn.commit()
    conn.close()

    return jsonify({'message': 'Practical appointment accepted successfully'}), 200

@practical_appointment.route('/practical_appointments/<int:slot_id>/reject', methods=['POST'])
def reject_practical_appointment(slot_id):
    conn = get_db_connection()
    cursor = conn.cursor()
    cursor.execute("UPDATE practical_calendar_slots SET status = 'rejected' WHERE id = ?", (slot_id,))
    conn.commit()

    # Check if any rows were updated
    if cursor.rowcount == 0:
        return jsonify({'message': 'Practical appointment not found or already rejected'}), 404

    conn.close()
    return jsonify({'message': 'Practical appointment request rejected successfully'}), 200

@practical_appointment.route('/practical_appointments/status/<string:nic>', methods=['GET'])
def get_practical_appointment_status(nic):
    conn = get_db_connection()
    cursor = conn.cursor()
    cursor.execute("SELECT * FROM practical_calendar_slots WHERE nic = ? ORDER BY id DESC LIMIT 1", (nic,))
    appointment = cursor.fetchone()
    conn.close()

    if appointment:
        return jsonify({
            'id': appointment['id'],
            'start_time': appointment['start_time'],
            'end_time': appointment['end_time'],
            'status': appointment['status'],
            'nic': appointment['nic']
        }), 200
    else:
        return jsonify({'message': 'No practical appointment found for this NIC'}), 404
