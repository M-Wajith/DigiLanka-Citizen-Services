import React, { useEffect, useState } from "react";
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap-icons/font/bootstrap-icons.css';
import { useNavigate } from 'react-router-dom';
import { Navbar, Nav, Container, Dropdown ,  Modal, Button} from 'react-bootstrap';
import axios from "axios";

export default function Newpassport() {
    const navigate = useNavigate();
    const [applications, setApplications] = useState([]);
    const [selectedApplication, setSelectedApplication] = useState(null);
    const [showModal, setShowModal] = useState(false);
    const [loading, setLoading] = useState(null);

    const handleLogout = () => {
        console.log("Logging out...");
        navigate('/login');
    };

    const handleVerifiedpassport = () => {
        navigate('/verified-passport'); 
    };
    const handlePrintedpassport = () => {
        navigate('/Printed-passport'); 
    };
    const handleDispatchedpassport = () =>{
        navigate('/dispatched-passport');
    };
    
    useEffect(() => {
        fetchApplications();
    }, []);

    const fetchApplications = async () => {
        try {
            const response = await fetch('http://localhost:5000/passport/applications'); // Adjust URL if needed
            const data = await response.json();
            setApplications(data);
        } catch (error) {
            console.error("Error fetching applications:", error);
        }
    };
    const handleShowDetails = (application) => {
        setSelectedApplication(application);
        setShowModal(true);
    };

    const handleVerify = async (nicNumber) => {
        setLoading(nicNumber); // Set loading for the clicked button
        try {
            const response = await axios.post(`http://127.0.0.1:5000/passport/verify/${nicNumber}`);
            alert(response.data.message); // Show success message

            // Remove the verified application from the UI
            setApplications((prevApps) => prevApps.filter(app => app.nicNumber !== nicNumber));
        } catch (error) {
            console.error("Verification Error:", error);
            alert("Error verifying application.");
        }
        setLoading(null); // Reset loading state
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
                        width: '79%',
                    }}>
                        <h4 className="text-primary text-center">New Passport Applications</h4>
                        
                            {/* ✅ Display message when applications array is empty */}
                            {applications.length === 0 ? (
                                <p className="text-center text-muted">No new passport applications.</p>
                            ) : (
                                applications.map((app, index) => (
                                    <div key={index} className="mt-3 p-3 bg-white shadow-sm rounded">
                                        <div className="d-flex justify-content-between align-items-center">
                                            {/* Left Side: Application Details */}
                                            <div onClick={() => handleShowDetails(app)} style={{ cursor: "pointer", flex: 1 }}>
                                                <p className="mb-2"><strong>Name:</strong> {app.surname} {app.otherName}</p>
                                                <p className="mb-2"><strong>Date of Birth:</strong> {app.dateOfBirth}</p>
                                                <p className="mb-2"><strong>Permanent Address:</strong> {app.permanentAddress}</p>
                                                <p className="mb-2"><strong>Old NIC Number:</strong> {app.nicNumber}</p>
                                            </div>

                                            {/* Right Side: Buttons */}
                                            <div className="d-flex" style={{ minWidth: "200px", justifyContent: "flex-end" }}>
                                                <button className="btn btn-success me-2" 
                                                    onClick={() => handleVerify(app.nicNumber)}
                                                    disabled={loading === app.nicNumber}
                                                >
                                                    Verify
                                                </button>
                                                <button className="btn btn-danger">Delete</button>
                                            </div>
                                        </div>
                                    </div>
                                ))
                            )}
                        
                    </div>

                </div>
            </Container>

            {/* Modal for Full Details */}
            <Modal show={showModal} onHide={() => setShowModal(false)} centered>
                <Modal.Header closeButton className="bg-primary text-white">
                    <Modal.Title>Application Details</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    {selectedApplication && (
                        <div className="px-3">
                            <h5 className="text-center text-primary fw-bold mb-3">Personal Information</h5>
                            <div className="row">
                                <div className="col-md-6 mb-2"><strong>Name:</strong> {selectedApplication.surname} {selectedApplication.otherName}</div>
                                <div className="col-md-6 mb-2"><strong>Date of Birth:</strong> {selectedApplication.dateOfBirth}</div>
                                <div className="col-md-6 mb-2"><strong>Permanent Address:</strong> {selectedApplication.permanentAddress}</div>
                                <div className="col-md-6 mb-2"><strong>NIC Number:</strong> {selectedApplication.nicNumber}</div>
                                <div className="col-md-6 mb-2"><strong>Profession:</strong> {selectedApplication.profession}</div>
                                <div className="col-md-6 mb-2"><strong>Phone:</strong> {selectedApplication.phoneNumber}</div>
                                <div className="col-md-6 mb-2"><strong>Email:</strong> {selectedApplication.emailAddress}</div>
                            </div>

                            <h5 className="text-center text-primary fw-bold mt-4">Uploaded Documents</h5>
                            <div className="d-flex flex-wrap justify-content-center mt-3">
                                <div className="m-2 text-center">
                                    <img src={`data:image/png;base64,${selectedApplication.nicFrontImage}`} alt="NIC Front" className="img-thumbnail border border-primary shadow" width="150" />
                                    <p className="fw-bold text-secondary">NIC Front</p>
                                </div>
                                <div className="m-2 text-center">
                                    <img src={`data:image/png;base64,${selectedApplication.nicBackImage}`} alt="NIC Back" className="img-thumbnail border border-primary shadow" width="150" />
                                    <p className="fw-bold text-secondary">NIC Back</p>
                                </div>
                                <div className="m-2 text-center">
                                    <img src={`data:image/png;base64,${selectedApplication.birthCertificateFront}`} alt="Birth Cert Front" className="img-thumbnail border border-primary shadow" width="150" />
                                    <p className="fw-bold text-secondary">Birth Cert Front</p>
                                </div>
                                <div className="m-2 text-center">
                                    <img src={`data:image/png;base64,${selectedApplication.birthCertificateBack}`} alt="Birth Cert Back" className="img-thumbnail border border-primary shadow" width="150" />
                                    <p className="fw-bold text-secondary">Birth Cert Back</p>
                                </div>
                                <div className="m-2 text-center">
                                    <img src={`data:image/png;base64,${selectedApplication.photoReceiptImage}`} alt="Photo Receipt" className="img-thumbnail border border-primary shadow" width="150" />
                                    <p className="fw-bold text-secondary">Photo Receipt</p>
                                </div>
                            </div>
                        </div>
                    )}
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={() => setShowModal(false)}>Close</Button>
                </Modal.Footer>
            </Modal>

        </>
    );
}
