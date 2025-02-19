import React, { useEffect, useState } from "react";
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap-icons/font/bootstrap-icons.css';
import { useNavigate } from 'react-router-dom';
import { Navbar, Nav, Container, Dropdown, Modal, Button } from 'react-bootstrap';
import axios from "axios";

export default function Dispatchnic(){


    const navigate = useNavigate();
    const [applications, setApplications] = useState([]);
    const [selectedApplication, setSelectedApplication] = useState(null);
    const [loading, setLoading] = useState(null);

        const handleLogout = () => {
            // Implement logout logic here
            console.log("Logging out...");
            // Redirect to login page or handle session end
            navigate('/login');
        };

        const handleNewnic = () => {
            navigate('/nic-dashboard'); 
        };

        const handleVerifiednic = () => {
            navigate('/verified-nic'); 
        };
        const handlePrintednic = () => {
            navigate('/Printed-nic'); 
        };
        const handleDispatchednic = () =>{
            navigate('/dispatched-nic');
        };

        // Fetch applications from backend API
        useEffect(() => {
            fetch("http://localhost:5000/api/dispatch_nic") // Update with your actual backend URL
                .then(response => response.json())
                .then(data => {
                    console.log("Fetched Data:", data); // Debugging log
                    if (Array.isArray(data.verified_applications)) {
                        setApplications(data.verified_applications);
                    } else {
                        console.error("Error: Data is not an array", data);
                        setApplications([]); // Ensure it's always an array
                    }
                })
                
                .catch(error => {
                    console.error("Error fetching applications:", error);
                    setApplications([]); // Set to empty array on failure
                });
        }, []);

    return(<>
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
                                                                        <a href="#" className="dd-block p-2 text-secondary"
                                                                            style={{ 
                                                                                borderRadius: '5px', 
                                                                                transition: 'background 0.3s', 
                                                                                display: 'block', 
                                                                                textDecoration: 'none', 
                                                                                whiteSpace: 'nowrap' 
                                                                            }}
                                                                            onMouseOver={(e) => e.target.style.background = '#d0d5db'}
                                                                            onMouseOut={(e) => e.target.style.background = 'transparent'}
                                                                            onClick={handleNewnic}
                                                                        >
                                                                            New Applications
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
                                                                            onClick={handleVerifiednic}
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
                                                                            onClick={handlePrintednic}
                                                                        >
                                                                            Printed Applications
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
                                                                            onMouseOver={(e) => e.target.style.background = '#d0d5db'}
                                                                            onMouseOut={(e) => e.target.style.background = 'transparent'}
                                                                            onClick={handleDispatchednic}
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
                                                                width: '79%',
                                                            }}>
                                                                <h4 className="text-primary text-center">Dispatched Applications</h4>
                                                                
                                                                    {/* ✅ Display message when applications array is empty */}
                                                                    {applications.length === 0 ? (
                                                                        <p className="text-center text-muted">No dispatched nic applications.</p>
                                                                    ) : (
                                                                        applications.map((app, index) => (
                                                                            <div key={index} className="mt-3 p-3 bg-white shadow-sm rounded">
                                                                                <div className="d-flex justify-content-between align-items-center">
                                                                                    {/* Left Side: Application Details */}
                                                                                    <div style={{ cursor: "pointer", flex: 1 }}>
                                                                                    <p><strong>Name:</strong> {app.name}</p>
                                                                                    <p><strong>Date of Birth:</strong> {app.date_of_birth}</p>
                                                                                    <p><strong>Permanent Address:</strong> {app.permanent_address}</p>
                                                                                    <p><strong>Old NIC Number:</strong> {app.old_nic_number}</p>
                                                                                    </div>
                                        
                
                                                                                </div>
                                                                            </div>
                                                                        ))
                                                                    )}
                                                                
                                                            </div>
                                        
                                                        </div>
                                                    </Container>
    </>)
}