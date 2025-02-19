CREATE TABLE IF NOT EXISTS "NIC_officers" (
        "NIC_officer_id"        INTEGER NOT NULL UNIQUE,
        "user_id"       INTEGER NOT NULL,
        PRIMARY KEY("NIC_officer_id" AUTOINCREMENT),
        FOREIGN KEY("user_id") REFERENCES "Users"("user_id")
);
CREATE TABLE IF NOT EXISTS "Divisional_officers" (
        "divisional_officer_id" INTEGER NOT NULL UNIQUE,
        "user_id"       INTEGER NOT NULL,
        PRIMARY KEY("divisional_officer_id" AUTOINCREMENT),
        FOREIGN KEY("user_id") REFERENCES "Users"("user_id")
);
CREATE TABLE IF NOT EXISTS "License_officers" (
        "license_officer_id"    INTEGER NOT NULL UNIQUE,
        "user_id"       INTEGER NOT NULL,
        PRIMARY KEY("license_officer_id" AUTOINCREMENT),
        FOREIGN KEY("user_id") REFERENCES "Users"("user_id")
);
CREATE TABLE IF NOT EXISTS "Users" (
        "user_id"       INTEGER NOT NULL UNIQUE,
        "username"      TEXT NOT NULL,
        "password"      TEXT NOT NULL UNIQUE,
        PRIMARY KEY("user_id" AUTOINCREMENT)
);
CREATE TABLE IF NOT EXISTS "Passport_officers" (
        "passport_officer_id"   INTEGER NOT NULL UNIQUE,
        "user_id"       INTEGER NOT NULL,
        PRIMARY KEY("passport_officer_id" AUTOINCREMENT),
        FOREIGN KEY("user_id") REFERENCES "Users"("user_id")
);
CREATE TABLE IF NOT EXISTS "citizens" (
        "first_name"    TEXT NOT NULL,
        "middle_name"   TEXT,
        "last_name"     TEXT NOT NULL,
        "date_of_birth" DATE NOT NULL,
        "gender"        TEXT NOT NULL CHECK("gender" IN ('Male', 'Female', 'Other')),
        "nic_number"    TEXT NOT NULL UNIQUE,
        "birth_certificate_number"      TEXT NOT NULL UNIQUE,
        "permanent_address"     TEXT NOT NULL,
        "captured_face_image"   TEXT NOT NULL,
        "birth_certificate_front"       BLOB NOT NULL,
        "birth_certificate_back"        BLOB NOT NULL
);
CREATE TABLE citizen_passport_application (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    travel_document TEXT NOT NULL,
    surname TEXT NOT NULL,
    other_name TEXT NOT NULL,
    nic_number TEXT NOT NULL UNIQUE,
    permanent_address TEXT NOT NULL,
    district TEXT NOT NULL,
    date_of_birth DATE NOT NULL,
    birth_certificate_number TEXT NOT NULL UNIQUE,
    gender TEXT NOT NULL CHECK(gender IN ('Male', 'Female', 'Other')),
    profession TEXT NOT NULL,
    dual_citizenship_number TEXT, -- Optional field
    phone_number TEXT NOT NULL,
    email_address TEXT NOT NULL,
    nic_front_image BLOB NOT NULL,
    nic_back_image BLOB NOT NULL,
    birth_certificate_front BLOB NOT NULL,
    birth_certificate_back BLOB NOT NULL,
    photo_receipt_image BLOB NOT NULL
);
CREATE TABLE verified_passport (
    id INTEGER PRIMARY KEY AUTOINCREMENT,   -- Unique ID for each record
    birth_certificate_number TEXT NOT NULL, -- Birth Certificate Number
    surname TEXT NOT NULL,                   -- Surname
    other_name TEXT NOT NULL,                 -- Other Name(s)
    nic_number TEXT UNIQUE NOT NULL,          -- NIC Number (should be unique)
    verified_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP -- Timestamp of verification
);
CREATE TABLE printed_passport (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    birth_certificate_number TEXT NOT NULL,
    surname TEXT NOT NULL,
    other_name TEXT NOT NULL,
    nic_number TEXT NOT NULL UNIQUE,
    printed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE TABLE IF NOT EXISTS "old_passport_status" (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nic_number TEXT NOT NULL UNIQUE,
    status TEXT NOT NULL CHECK(status IN ('Applied', 'Verified', 'Printed', 'Dispatched')),
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY(nic_number) REFERENCES citizen_passport_application(nic_number) ON DELETE CASCADE
);
CREATE TABLE passport_status (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nic_number TEXT NOT NULL UNIQUE,
    status TEXT NOT NULL CHECK(status IN ('Applied', 'Verified', 'printed', 'dispatched')),
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY(nic_number) REFERENCES citizen_passport_application(nic_number) ON DELETE CASCADE
);
CREATE TABLE dispatched_passport (
    id INTEGER PRIMARY KEY AUTOINCREMENT,  -- Unique ID for each record
    birth_certificate_number TEXT NOT NULL,  -- Birth certificate number
    surname TEXT NOT NULL,  -- Surname of the applicant
    other_name TEXT NOT NULL,  -- Other name of the applicant
    nic_number TEXT NOT NULL UNIQUE,  -- National Identity Card number (unique)
    dispatch_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP  -- Timestamp of when the application was dispatched
);
CREATE TABLE written_calendar_slots (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    start_time TEXT NOT NULL,
    end_time TEXT NOT NULL,
    status TEXT CHECK(status IN ('requested', 'booked', 'rejected')) NOT NULL DEFAULT 'requested',
    nic TEXT NOT NULL
);
CREATE TABLE calendar_slots (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    start_time TEXT NOT NULL,
    end_time TEXT NOT NULL,
    status TEXT NOT NULL CHECK(status IN ('requested', 'booked', 'rejected')),
    nic TEXT NOT NULL
);
CREATE TABLE practical_calendar_slots (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    start_time TEXT NOT NULL,
    end_time TEXT NOT NULL,
    status TEXT CHECK( status IN ('requested', 'booked', 'rejected') ) NOT NULL DEFAULT 'requested',
    nic TEXT NOT NULL
);
CREATE TABLE citizen_nic_application (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    surname VARCHAR(255) NOT NULL,
    gender      TEXT NOT NULL CHECK("gender" IN ('Male', 'Female', 'Other')),
    date_of_birth DATE NOT NULL,
    civil_status VARCHAR(50),
    profession VARCHAR(255),
    birth_place VARCHAR(255),
    district VARCHAR(100),
    permanent_address TEXT,
    police_complaint BLOB,
    photo_receipt BLOB,
    birth_certficate_front BLOB,
    birth_certficate_back BLOB,
    email VARCHAR(255) UNIQUE NOT NULL,
    phone_number VARCHAR(20) NOT NULL,
    old_nic_number VARCHAR(50) UNIQUE
);
CREATE TABLE IF NOT EXISTS "nic_status_old" (
    old_nic_number TEXT PRIMARY KEY,
    status TEXT CHECK(status IN ('applied', 'verified', 'printed', 'dispatched')) NOT NULL
);
CREATE TABLE verified_nic (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    old_nic_number TEXT UNIQUE NOT NULL,
    date_of_birth TEXT NOT NULL,
    permanent_address TEXT NOT NULL
);
CREATE TABLE nic_status (
    old_nic_number TEXT,
    status TEXT NOT NULL CHECK(status IN ('applied', 'Verified', 'printed', 'dispatched')),
    PRIMARY KEY(old_nic_number)
);
CREATE TABLE printed_nic (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    old_nic_number TEXT UNIQUE NOT NULL,
    date_of_birth TEXT NOT NULL,
    permanent_address TEXT NOT NULL,
    printed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE TABLE dispatched_nic (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    old_nic_number TEXT UNIQUE NOT NULL,
    date_of_birth TEXT NOT NULL,
    permanent_address TEXT NOT NULL,
    dispatched_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);