# Digi Lanka Project - README

## Table of Contents
- [Project Overview](#project-overview)
- [System Requirements](#system-requirements)
- [Project Structure](#project-structure)
- [Installation and Setup](#installation-and-setup)
  - [Backend Setup (Flask)](#backend-setup-flask)
  - [Frontend Setup (React)](#frontend-setup-react)
  - [Mobile Application Setup (Android)](#mobile-application-setup-android)
  - [Database Setup (SQLite3)](#database-setup-sqlite3)
- [Running the Project](#running-the-project)
- [API Documentation](#api-documentation)
- [Troubleshooting](#troubleshooting)
- [Contributing](#contributing)

## Project Overview
Digi Lanka is a digital governance project that integrates a React frontend, a Flask-based backend, and an Android mobile application. The system facilitates document verification, appointment scheduling, and user authentication.

## System Requirements
Ensure you have the following installed:
- **Python** (Version 3.8 or above)
- **Node.js** (Version 16 or above)
- **npm** (Included with Node.js)
- **Android Studio** (Latest version for mobile app development)
- **SQLite3** (For database management)
- **Git** (For version control)

## Project Structure
```
project/
│-- backend/        # Flask backend
│-- frontend/       # React frontend
│-- Digi Lanka/     # Android mobile application
│-- database/       # SQLite3 database storage
│-- digilanka.sql   # Database schema
```

## Installation and Setup

### Backend Setup (Flask)
Navigate to the backend directory:
```sh
cd backend
```
Create a virtual environment (optional but recommended):
```sh
python -m venv venv
source venv/bin/activate   # On Windows: venv\Scripts\activate
```
Install dependencies:
```sh
pip install -r requirements.txt
```
Run the Flask server:
```sh
python app.py
```

### Frontend Setup (React)
Navigate to the frontend directory:
```sh
cd frontend/digilanka
```
Install dependencies:
```sh
npm install
```
Start the development server:
```sh
npm start
```

### Mobile Application Setup (Android)
1. Open Android Studio and import the **Digi Lanka** folder.
2. Sync Gradle by clicking **File > Sync Project with Gradle Files**.
3. Connect an emulator or a physical device.
4. Build and run the application.

### Database Setup (SQLite3)
SQLite3 is used as the database for this project. The database file is stored locally and must be correctly referenced in the project files.

#### **Step 1: Create an SQLite Database**
- Open a terminal or command prompt and navigate to the desired database storage location.
- Run the following command to create an empty database file:
  ```sh
  sqlite3 digilanka.db
  ```
  *(This will create an empty SQLite3 database file named `digilanka.db` in the current directory.)*

#### **Step 2: Import the Schema**
- Use **DB Browser for SQLite** or any SQLite3 tool to execute the `digilanka.sql` schema file and create the tables.
- Alternatively, you can run the following command in the terminal:
  ```sh
  sqlite3 digilanka.db < digilanka.sql
  ```
  *(This will execute the schema and create the necessary tables in the `digilanka.db` database.)*

#### **Step 3: Update Database Path in the Project**
- The database file path is **hardcoded** in the project files.
- Open the **backend code** (e.g., `app.py` or any relevant file) and find the following line:
  ```python
  conn = sqlite3.connect('E:/Wajith/Horizon/Sem 8/project/Database/digilanka.db', check_same_thread=False)
  ```
- Update the path to match your local system, ensuring it correctly points to `digilanka.db`.
  ```python
  conn = sqlite3.connect('your/local/path/to/digilanka.db', check_same_thread=False)
  ```

## Running the Project
1. **Start the backend server (Flask)**:
   ```sh
   python app.py
   ```
2. **Start the frontend server (React)**:
   ```sh
   npm start
   ```
3. **Run the Android application via Android Studio**.

## API Documentation
The Flask backend exposes APIs for authentication, data retrieval, and updates. Full API documentation is available in the backend folder under `api_docs.md`.

## Troubleshooting
- **Backend Issues**: Ensure dependencies are installed correctly and SQLite3 is accessible.
- **Frontend Issues**: Run `npm install` again if dependencies are missing.
- **Android Issues**: Check if the correct SDK versions are installed in Android Studio.

## Contributing
Pull requests are welcome! Ensure code follows best practices and is well-documented.

