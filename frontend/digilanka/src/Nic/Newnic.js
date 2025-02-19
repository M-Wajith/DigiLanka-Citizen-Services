import React, { useEffect, useState } from "react";
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap-icons/font/bootstrap-icons.css';
import { useNavigate } from 'react-router-dom';
import { Navbar, Nav, Container, Dropdown, Modal, Button } from 'react-bootstrap';
import axios from "axios";

export default function Newnic(){
    const navigate = useNavigate();
    const [applications, setApplications] = useState([]);
    const [selectedApplication, setSelectedApplication] = useState(null);
    const [showModal, setShowModal] = useState(false);
    const [loading, setLoading] = useState(null);

        const handleLogout = () => {
            // Implement logout logic here
            console.log("Logging out...");
            // Redirect to login page or handle session end
            navigate('/login');
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
        fetch("http://localhost:5000/api/nicapplications") // Update with your actual backend URL
            .then(response => response.json())
            .then(data => setApplications(data))
            .catch(error => console.error("Error fetching applications:", error));
    }, []);

    const handleShowDetails = (app) => {
        fetch(`http://localhost:5000/api/application/${app.old_nic_number}`)
            .then(response => response.json())
            .then(data => {
                setSelectedApplication(data);
                setShowModal(true);
            })
            .catch(error => console.error("Error fetching application details:", error));
    };
    

    const handleVerify = async (old_nic_number) => {
        setLoading(old_nic_number); // Set loading for the clicked button
        try {
            const response = await axios.post(`http://127.0.0.1:5000/api/verify/${old_nic_number}`);
            alert(response.data.message); // Show success message

            // Remove the verified application from the UI
            setApplications((prevApps) => prevApps.filter(app => app.old_nic_number !== old_nic_number));
        } catch (error) {
            console.error("Verification Error:", error);
            alert("Error verifying application.");
        }
        setLoading(null); // Reset loading state
    };
        return(
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
                                    <h4 className="text-primary text-center">New NIC Applications</h4>
                                    
                                        {/* ✅ Display message when applications array is empty */}
                                        {applications.length === 0 ? (
                                            <p className="text-center text-muted">No new nic applications.</p>
                                        ) : (
                                            applications.map((app, index) => (
                                                <div key={index} className="mt-3 p-3 bg-white shadow-sm rounded">
                                                    <div className="d-flex justify-content-between align-items-center">
                                                        {/* Left Side: Application Details */}
                                                        <div onClick={() => handleShowDetails(app)} style={{ cursor: "pointer", flex: 1 }}>
                                                        <p><strong>Name:</strong> {app.name}</p>
                                                        <p><strong>Date of Birth:</strong> {app.date_of_birth}</p>
                                                        <p><strong>Permanent Address:</strong> {app.permanent_address}</p>
                                                        <p><strong>Old NIC Number:</strong> {app.old_nic_number}</p>
                                                        </div>
            
                                                        {/* Right Side: Buttons */}
                                                        <div className="d-flex" style={{ minWidth: "200px", justifyContent: "flex-end" }}>
                                                            <button className="btn btn-success me-2" 
                                                                onClick={() => handleVerify(app.old_nic_number)}
                                                                disabled={loading === app.old_nic_number}
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

                       {/* Modal for Application Details */}
{/* Modal for Application Details */}
<Modal show={showModal} onHide={() => setShowModal(false)} size="lg">
    <Modal.Header closeButton>
        <Modal.Title className="text-primary">NIC Application Details</Modal.Title>
    </Modal.Header>
    <Modal.Body>
        {selectedApplication && (
            <div className="px-3">
                <h5 className="text-center text-primary fw-bold mb-3">Personal Information</h5>
                <div className="row">
                    <div className="col-md-6"><strong>Name:</strong> {selectedApplication.name} {selectedApplication.surname}</div>
                    <div className="col-md-6"><strong>Gender:</strong> {selectedApplication.gender}</div>
                    <div className="col-md-6"><strong>Date of Birth:</strong> {selectedApplication.date_of_birth}</div>
                    <div className="col-md-6"><strong>Civil Status:</strong> {selectedApplication.civil_status}</div>
                    <div className="col-md-6"><strong>Profession:</strong> {selectedApplication.profession}</div>
                    <div className="col-md-6"><strong>Birth Place:</strong> {selectedApplication.birth_place}</div>
                    <div className="col-md-6"><strong>District:</strong> {selectedApplication.district}</div>
                    <div className="col-md-6"><strong>Email:</strong> {selectedApplication.email}</div>
                    <div className="col-md-6"><strong>Phone Number:</strong> {selectedApplication.phone_number}</div>
                    <div className="col-md-12"><strong>Permanent Address:</strong> {selectedApplication.permanent_address}</div>
                </div>

                <h5 className="text-center text-primary fw-bold mt-4">Uploaded Documents</h5>
                <div className="row justify-content-center mt-3">
                    <div className="col-12 col-md-6 col-lg-3 m-2 text-center">
                        {selectedApplication.police_complaint && (
                            <>
                                <strong>Police Complaint</strong>
                                <img 
                                    src={`data:image/png;base64,${selectedApplication.police_complaint}`} 
                                    alt="Police Complaint" 
                                    className="img-fluid rounded shadow"
                                    style={{ maxWidth: '100%', height: 'auto' }} 
                                />
                            </>
                        )}
                    </div>
                    <div className="col-12 col-md-6 col-lg-3 m-2 text-center">
                        {selectedApplication.photo_receipt && (
                            <>
                                <strong>Photo Receipt</strong>
                                <img 
                                    src={`data:image/png;base64,${selectedApplication.photo_receipt}`} 
                                    alt="Photo Receipt" 
                                    className="img-fluid rounded shadow"
                                    style={{ maxWidth: '100%', height: 'auto' }} 
                                />
                            </>
                        )}
                    </div>
                    <div className="col-12 col-md-6 col-lg-3 m-2 text-center">
                        {selectedApplication.birth_certificate_front && (
                            <>
                                <strong>Birth Certificate (Front)</strong>
                                <img 
                                    src={`data:image/png;base64,${selectedApplication.birth_certificate_front}`} 
                                    alt="Birth Certificate Front" 
                                    className="img-fluid rounded shadow"
                                    style={{ maxWidth: '100%', height: 'auto' }} 
                                />
                            </>
                        )}
                    </div>
                    <div className="col-12 col-md-6 col-lg-3 m-2 text-center">
                        {selectedApplication.birth_certificate_back && (
                            <>
                                <strong>Birth Certificate (Back)</strong>
                                <img 
                                    src={`data:image/png;base64,${selectedApplication.birth_certificate_back}`} 
                                    alt="Birth Certificate Back" 
                                    className="img-fluid rounded shadow"
                                    style={{ maxWidth: '100%', height: 'auto' }} 
                                />
                            </>
                        )}
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
        )
}