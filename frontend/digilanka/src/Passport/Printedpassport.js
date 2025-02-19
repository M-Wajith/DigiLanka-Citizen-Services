import React, { useState, useEffect } from "react";
import axios from "axios";
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap-icons/font/bootstrap-icons.css';
import { useNavigate } from 'react-router-dom';
import { Navbar, Nav, Container, Dropdown } from 'react-bootstrap';

export default function Printedpassport(){
    const navigate = useNavigate();

        const handleLogout = () => {
            console.log("Logging out...");
            navigate('/login');
        };

        const handleNewpassport = () => {
            navigate('/passport-dashboard'); 
        };
        const handleVerifiedpassport = () => {
            navigate('/verified-passport'); 
        };
        const handleDispatchedpassport = () => {
            navigate('/dispatched-passport'); 
        };

        const [passportData, setPassportData] = useState([]);
    const [error, setError] = useState(null);

    useEffect(() => {
        // Fetch passport data on component mount
        axios
            .get("http://localhost:5000/api/printed_passport") // Make sure to use the correct endpoint URL
            .then((response) => {
                setPassportData(response.data); // Set the fetched data into state
            })
            .catch((error) => {
                setError(error.response ? error.response.data : "An error occurred");
            });
    }, []);

    // Dispatch function to call backend and move data to dispatched_passport
    const dispatchApplication = (nicNumber) => {
        axios
            .post(`http://localhost:5000/api/printed_passport/dispatch/${nicNumber}`)
            .then((response) => {
                // Remove the dispatched application from the list (Optimistic UI update)
                setPassportData((prevData) =>
                    prevData.filter((data) => data.nicNumber !== nicNumber)
                );
                alert(response.data.message); // Show success message
            })
            .catch((error) => {
                alert(error.response ? error.response.data.error : "An error occurred");
            });
    };


    return(
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
                                            onClick={handleVerifiedpassport}
                                        >
                                            Verified Applications
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
                            <div className="col-md-8 p-4 shadow-sm" style={{ minHeight: "80vh", backgroundColor: "#e9ecef", borderRadius: "10px", width: "79%" }}>
                                    <h4 className="text-primary text-center">Printed Passport Applications</h4>
                                    
                                    
                                        {/* Show this message only if there are no records */}
                                        {passportData.length === 0 ? (
                                            <p className="text-center text-muted">No printed applications available.</p>
                                        ) : (
                                            passportData.map((data, index) => (
                                                <div key={index} className="mt-3 p-3 bg-white shadow-sm rounded">
                                                <div key={index} className="d-flex justify-content-between align-items-center">
                                                    <div>
                                                        <p className="mb-2"><strong>Surname:</strong> {data.surname}</p>
                                                        <p className="mb-2"><strong>Other Name:</strong> {data.otherName}</p>
                                                        <p className="mb-2"><strong>NIC Number:</strong> {data.nicNumber}</p>
                                                        <p className="mb-2"><strong>Birth Certificate Number:</strong> {data.birthCertificateNumber}</p>
                                                    </div>
                                                    <div>
                                                        <button className="btn btn-success me-2" onClick={() => dispatchApplication(data.nicNumber)}>Dispatch</button>
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
    )
}