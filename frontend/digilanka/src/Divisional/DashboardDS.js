import React, { useState } from "react";
import { useNavigate } from 'react-router-dom';
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap-icons/font/bootstrap-icons.css';
import { Navbar, Nav, Container, Dropdown } from 'react-bootstrap';

export default function DashboardDS() {

    const [loading, setLoading] = useState(false);
    const navigate = useNavigate(); // Initialize useNavigate

    // Handle button click to navigate to AddCitizen
    const handleButtonClick = () => {
        setLoading(true); // Set loading state to true
        setTimeout(() => {
            navigate('/add-citizen'); // Navigate to the Add Citizen page
        }, 500); // Adjust the timeout duration as needed
    };
    const handleEditButtonClick = () => {
        setLoading(true); // Set loading state to true
        setTimeout(() => {
            navigate('/edit-citizen'); // Navigate to the Add Citizen page
        }, 500); // Adjust the timeout duration as needed
    };

    // Handle logout
    const handleLogout = () => {
        // Implement logout logic here
        console.log("Logging out...");
        // Redirect to login page or handle session end
        navigate('/login');
    };

    return (
        <>
            <Navbar
                bg="primary"
                variant="light"
                expand="lg"
                className="px-3 shadow-sm"
                style={{ borderBottom: '1px solid #e5e5e5' }}
            >
                <Container fluid>
                    <Navbar.Brand href="#home" className="d-flex align-items-center">
                        <img
                            src="Digi_Goverment.png"
                            width="40"
                            height="40"
                            className="d-inline-block align-top"
                            alt="Logo"
                        />
                        <img
                            src="heading.png"
                            width="250"
                            height="40"
                            className="d-inline-block align-top ms-2"
                            alt="Title"
                        />
                    </Navbar.Brand>
                    <Navbar.Toggle aria-controls="basic-navbar-nav" />
                    <Navbar.Collapse id="basic-navbar-nav">
                        <Nav className="ms-auto d-flex align-items-center">
                        <Nav.Link
                            href="#home"
                            className="text-light d-flex align-items-center"
                            style={{
                                fontSize: '16px',
                                padding: '10px 15px',
                                color: '#007bff', // Primary color text for active
                                textDecoration: 'underline', // Underline effect for active link
                                fontWeight: 'bold', // Optional: Make text bold
                                marginRight: '5px', // Spacing between nav items
                            }}
                        >
                            Home
                        </Nav.Link>
                            <Nav.Link
                                href="#add-citizen"
                                className="text-light"
                                style={{ fontSize: '16px', padding: '10px 15px' }}
                                onClick={handleButtonClick}
                            >
                                Add Citizen
                            </Nav.Link>
                            <Nav.Link
                                href="#edit-citizen"
                                className="text-light"
                                style={{ fontSize: '16px', padding: '10px 15px' }}
                                onClick={handleEditButtonClick}
                            >
                                Edit Citizen
                            </Nav.Link>
                            <Dropdown align="end" className="d-flex align-items-center">
                                <Dropdown.Toggle
                                    variant="link"
                                    id="profile-dropdown"
                                    className="text-light d-flex align-items-center"
                                    style={{
                                        fontSize: '20px',
                                        background: 'transparent',
                                        border: 'none',
                                        boxShadow: 'none',
                                        cursor: 'pointer',
                                    }}
                                >
                                    <i className="bi bi-person-circle" style={{ fontSize: '20px' }}></i>
                                </Dropdown.Toggle>
                                <Dropdown.Menu>
                                    <Dropdown.Item onClick={handleLogout}>Logout</Dropdown.Item>
                                </Dropdown.Menu>
                            </Dropdown>
                        </Nav>
                    </Navbar.Collapse>
                </Container>
            </Navbar>
            <div className="container-fluid mt-4 text-center">
                <h1
                    style={{
                        display: 'inline-block', // Ensures the underline fits the text width
                        borderBottom: '2px solid #007bff', // Blue color
                        paddingBottom: '10px', // Space between text and underline
                    }}
                >
                    Divisional Secretariat Sri Lanka
                </h1>
            </div>

            <div className="container-fluid" style={{ minHeight: '60vh', display: 'flex', alignItems: 'center' }}>
                <div className="row w-100">
                    <div className="col-12 col-md-5 d-flex justify-content-center justify-content-md-end mb-3">
                        <button 
                            type="button" 
                            className="btn btn-primary" 
                            style={{ fontSize: '1.5rem', padding: '15px 30px' }} 
                            onClick={handleButtonClick} // Add onClick event
                        >
                            Add Citizen
                        </button>
                    </div>
                    <div className="col-2">
                    </div>
                    <div className="col-12 col-md-5 d-flex justify-content-center justify-content-md-start mb-3">
                        <button type="button" className="btn btn-primary" style={{ fontSize: '1.5rem', padding: '15px 30px' }}>Edit Citizen</button>
                    </div>
                </div>
            </div>
        </>
    );
}
