import React, { useState, useEffect } from "react";
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap-icons/font/bootstrap-icons.css';
import { useNavigate } from 'react-router-dom';
import { Navbar, Nav, Container, Dropdown } from 'react-bootstrap';
import axios from "axios";

export default function Verifiedpassport() {
    const navigate = useNavigate();
    const [verifiedApplications, setVerifiedApplications] = useState([]);

    useEffect(() => {
        axios.get("http://127.0.0.1:5000/api/verified_passport")  // Adjust the API endpoint as needed
            .then(response => {
                setVerifiedApplications(response.data);
            })
            .catch(error => {
                console.error("Error fetching verified applications:", error);
            });
    }, []);

    const handlePrint = (nicNumber) => {
        fetch(`http://127.0.0.1:5000/api/verified_passport/print/${nicNumber}`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
        })
        .then((response) => response.json())
        .then((data) => {
            if (data.message) {
                alert("Application moved to Printed Passports");
                // Remove the application from the verified list
                setVerifiedApplications(prevApps => prevApps.filter(app => app.nicNumber !== nicNumber));
            } else {
                alert("Error: " + data.error);
            }
        })
        .catch((error) => {
            console.error("Error moving application:", error);
            alert("Failed to move application");
        });
    };

    const handleLogout = () => {
        console.log("Logging out...");
        navigate('/login');
    };

    const handleNewpassport = () => {
        navigate('/passport-dashboard'); 
    };

    const handlePrintedpassport = () => {
        navigate('/Printed-passport'); 
    };

    const handleDispatchedpassport = () => {
        navigate('/dispatched-passport'); 
    };

    return (
        <>
            {/* Navbar */}
            <Navbar bg="primary" variant="dark" expand="lg" className="px-3 shadow-sm">
                <Container fluid>
                    <Navbar.Brand href="#home" className="d-flex align-items-center">
                        <img src="Digi_Goverment.png" width="40" height="40" className="d-inline-block align-top" alt="Logo" />
                        <img src="heading.png" width="250" height="40" className="d-inline-block align-top ms-2" alt="Title" />
                    </Navbar.Brand>
                    <Navbar.Toggle aria-controls="basic-navbar-nav" />
                    <Navbar.Collapse id="basic-navbar-nav">
                        <Nav className="ms-auto d-flex align-items-center">
                            <Dropdown align="end" className="d-flex align-items-center">
                                <Dropdown.Toggle
                                    variant="link"
                                    id="profile-dropdown"
                                    className="text-light d-flex align-items-center"
                                    style={{ fontSize: '20px', background: 'transparent', border: 'none', boxShadow: 'none', cursor: 'pointer' }}
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

            {/* Main Layout */}
            <Container fluid className="mt-3 mx-3">
                <div className="row">
                    {/* Left Sidebar */}
                    <div className="col-md-2 p-4 shadow-sm me-4" style={{ 
                        minHeight: '80vh', 
                        backgroundColor: '#e9ecef', 
                        borderRadius: '10px' 
                    }}>
                        <h5 className="text-primary text-center fw-bold mb-4">Menu</h5>
                        <ul className="list-unstyled">
                            <li className="mb-2">
                                <a href="#" className="d-block p-2 text-secondary"
                                    style={{ 
                                        borderRadius: '5px', 
                                        transition: 'background 0.3s', 
                                        display: 'block', 
                                        textDecoration: 'none', 
                                        whiteSpace: 'nowrap' 
                                    }}
                                    onMouseOver={(e) => e.target.style.background = '#d0d5db'}
                                    onMouseOut={(e) => e.target.style.background = 'transparent'}
                                    onClick={handleNewpassport}
                                >
                                    New Applications
                                </a>
                            </li>
                            <li className="mb-2">
                                <a href="#" className="d-block p-2 fw-bold text-primary"
                                    style={{ 
                                        borderRadius: '5px', 
                                        transition: 'background 0.3s', 
                                        display: 'block', 
                                        textDecoration: 'none', 
                                        whiteSpace: 'nowrap' 
                                    }}
                                >
                                    Verified Applications
                                </a>
                            </li>
                            <li className="mb-2">
                                <a href="#" className="d-block p-2 text-secondary"
                                    style={{ 
                                        borderRadius: '5px', 
                                        transition: 'background 0.3s', 
                                        display: 'block', 
                                        textDecoration: 'none', 
                                        whiteSpace: 'nowrap' 
                                    }}
                                    onMouseOver={(e) => e.target.style.background = '#d0d5db'}
                                    onMouseOut={(e) => e.target.style.background = 'transparent'}
                                    onClick={handlePrintedpassport}
                                >
                                    Printed Applications
                                </a>
                            </li>
                            <li className="mb-2">
                                <a href="#" className="d-block p-2 text-secondary"
                                    style={{ 
                                        borderRadius: '5px', 
                                        transition: 'background 0.3s', 
                                        display: 'block', 
                                        textDecoration: 'none', 
                                        whiteSpace: 'nowrap' 
                                    }}
                                    onMouseOver={(e) => e.target.style.background = '#d0d5db'}
                                    onMouseOut={(e) => e.target.style.background = 'transparent'}
                                    onClick={handleDispatchedpassport}
                                >
                                    Dispatched Applications
                                </a>
                            </li>
                        </ul>
                    </div>

                    {/* Right Content Area */}
                    <div className="col-md-8 p-4 shadow-sm" style={{ 
                        minHeight: '80vh', 
                        backgroundColor: '#e9ecef', 
                        borderRadius: '10px',
                        width:'79%',
                    }}>
                        <h4 className="text-primary text-center">Verified Passport Applications</h4>
                            {verifiedApplications.length === 0 ? (
                                <p className="text-center text-muted">No verified applications available.</p>
                            ) : (
                                verifiedApplications.map((app, index) => (
                                    <div key={index} className="mt-3 p-3 bg-white shadow-sm rounded">
                                        <div className="d-flex justify-content-between align-items-center">
                                            <div>
                                            <p className="mb-2"><strong>Surname:</strong> {app.surname}</p>
                                            <p className="mb-2"><strong>Other Name:</strong> {app.otherName}</p>
                                            <p className="mb-2"><strong>NIC Number:</strong> {app.nicNumber}</p>
                                            <p className="mb-2"><strong>Birth Certificate Number:</strong> {app.birthCertificateNumber}</p>
                                            </div>
                                            <div>
                                                <button className="btn btn-success me-2" onClick={() => handlePrint(app.nicNumber)}>Print</button>
                                                <button className="btn btn-danger">Delete</button>
                                            </div>
                                        </div>
                                    </div>
                                ))
                            )}
                        
                    </div>
                </div>
            </Container>
        </>
    );
}
