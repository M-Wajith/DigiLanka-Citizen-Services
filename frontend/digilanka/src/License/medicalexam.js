import React, { useState, useEffect } from "react";
import FullCalendar from "@fullcalendar/react";
import dayGridPlugin from "@fullcalendar/daygrid";
import timeGridPlugin from "@fullcalendar/timegrid"; // Time-based views
import "bootstrap/dist/css/bootstrap.min.css";
import "bootstrap-icons/font/bootstrap-icons.css";
import { useNavigate } from "react-router-dom";
import { Navbar, Nav, Container, Dropdown , Modal, Button } from "react-bootstrap";
import axios from "axios";

export default function Medicalexam(){

    const navigate = useNavigate();

    const [appointments, setAppointments] = useState([]);
    const [selectedAppointment, setSelectedAppointment] = useState(null);
    const [showModal, setShowModal] = useState(false);
    
        const handleLogout = () => {
            console.log("Logging out...");
            navigate("/login");
        };
    
        const [events, setEvents] = useState([
            { title: "Exam 1", start: "2024-11-10T08:30:00", end: "2024-11-10T09:00:00", allDay: false },
            { title: "Exam 2", start: "2024-11-15T10:00:00", end: "2024-11-15T10:30:00", allDay: false }
        ]);

        const handlewritten = () => {
            navigate('/written'); 
        };
        const handlepractical = () => {
            navigate('/practical'); 
        };

         // Fetch Appointments from API
    useEffect(() => {
        fetch("http://localhost:5000/api/appointments") // Update with your API URL
            .then((res) => res.json())
            .then((data) => {
                const formattedEvents = data.map(appointment => ({
                    id: appointment.id,
                    title: `Request from ${appointment.nic}`,
                    start: new Date(appointment.start_time),
                    end: new Date(appointment.end_time),
                    status: appointment.status
                }));
                setAppointments(formattedEvents);
            })
            .catch(error => console.error("Error fetching appointments:", error));
    }, []);

    // Open Modal when event is clicked
    const handleEventClick = (clickInfo) => {
        const appointment = appointments.find(app => app.id === parseInt(clickInfo.event.id));
        setSelectedAppointment(appointment);
        setShowModal(true);
    };

    // Accept Appointment
    const handleAccept = () => {
        fetch(`http://localhost:5000/api/appointments/${selectedAppointment.id}/accept`, {
            method: "POST"
        }).then(() => {
            setAppointments(prev =>
                prev.map(app =>
                    app.id === selectedAppointment.id ? { ...app, status: "booked" } : app
                )
            );
            setShowModal(false);
        });
    };

    // Reject Appointment
    const handleReject = () => {
        fetch(`http://localhost:5000/api/appointments/${selectedAppointment.id}/reject`, {
            method: "POST"
        }).then(() => {
            setAppointments(prev =>
                prev.map(app =>
                    app.id === selectedAppointment.id ? { ...app, status: "rejected" } : app
                )
            );
            setShowModal(false);
        });
    };


        return (
            <>
            <Navbar bg="primary" variant="light" expand="lg" className="px-3 shadow-sm" style={{ borderBottom: '1px solid #e5e5e5' }}>
                            <Container fluid>
                                <Navbar.Brand href="#home" className="d-flex align-items-center">
                                    <img src="Digi_Goverment.png" width="40" height="40" className="d-inline-block align-top" alt="Logo" />
                                    <img src="heading.png" width="250" height="40" className="d-inline-block align-top ms-2" alt="Title" />
                                </Navbar.Brand>
                                <Navbar.Toggle aria-controls="basic-navbar-nav" />
                                <Navbar.Collapse id="basic-navbar-nav">
                                    <Nav className="ms-auto d-flex align-items-center">
                                        <Dropdown align="end" className="d-flex align-items-center">
                                            <Dropdown.Toggle variant="link" id="profile-dropdown" className="text-light d-flex align-items-center" style={{ fontSize: '20px', background: 'transparent', border: 'none', boxShadow: 'none', cursor: 'pointer' }}>
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

                        <Container fluid className="mt-3 mx-3">
                <div className="row">
                    {/* Left Sidebar */}
                    <div className="col-md-2 p-4 shadow-sm me-4" style={{ minHeight: '80vh', backgroundColor: '#e9ecef', borderRadius: '10px', width: '265px', overflow: 'hidden' }}>
                        <h5 className="text-primary text-center fw-bold mb-4">Menu</h5>
                        <ul className="list-unstyled">
                            <li className="mb-2">
                                <a href="#" className="d-block p-2 fw-bold text-primary" style={{ borderRadius: '5px', transition: 'background 0.3s', display: 'block', textDecoration: 'none', whiteSpace: 'nowrap' }}
                                    onMouseOver={(e) => e.target.style.background = '#d0d5db'}
                                    onMouseOut={(e) => e.target.style.background = 'transparent'}>
                                    Medical Exam Appointment
                                </a>
                            </li>
                            <li className="mb-2">
                                <a href="#" className="d-block p-2  text-secondary" style={{ borderRadius: '5px', transition: 'background 0.3s', display: 'block', textDecoration: 'none', whiteSpace: 'nowrap' }}
                                    onMouseOver={(e) => e.target.style.background = '#d0d5db'}
                                    onMouseOut={(e) => e.target.style.background = 'transparent'}
                                    onClick={handlewritten}
                                    >
                                    Written Exam Appointment
                                </a>
                            </li>
                            <li className="mb-2">
                                <a href="#" className="d-block p-2 text-secondary" style={{ borderRadius: '5px', transition: 'background 0.3s', display: 'block', textDecoration: 'none', whiteSpace: 'nowrap' }}
                                    onMouseOver={(e) => e.target.style.background = '#d0d5db'}
                                    onMouseOut={(e) => e.target.style.background = 'transparent'}
                                    onClick={handlepractical}
                                    >
                                    Practical Exam Appointment
                                </a>
                            </li>
                        </ul>
                    </div>

                    {/* Right Content Area */}
                    <div className="col-md-8 p-4 shadow-sm" style={{
                        minHeight: '80vh', backgroundColor: '#e9ecef', borderRadius: '10px', width: '76.35%',
                    }}>
                        <h4 className="text-primary text-center">Medical Exam Appointment</h4>

                        {/* FullCalendar */}
                        <div style={{ height: "460px", overflowY: "auto", border: "1px solid #ddd", borderRadius: "10px", background: "white" }}>
    <FullCalendar
        plugins={[dayGridPlugin, timeGridPlugin]}
        initialView="timeGridDay"
        slotDuration="00:30:00"
        slotMinTime="08:00:00"
        slotMaxTime="17:00:00"
        events={appointments}
        headerToolbar={{
            left: "prev,next today",
            center: "title",
            right: "dayGridMonth,timeGridWeek,timeGridDay",
        }}
        eventClick={handleEventClick} // Handle event click
        eventColor="blue"
        editable={true}
        scrollTime="08:00:00"
        allDaySlot={false}
        slotLabelInterval={"00:30:00"}
        slotLabelFormat={{ hour: "numeric", minute: "2-digit", hour12: true }}
    />
</div>

                    </div>
                </div>
            </Container>

            {/* Custom Styles */}
            <style>{`
        .fc-header-toolbar {
    position: sticky;
    top: 0;
    background: white;
    z-index: 1000;
    padding: 10px 0;
}
    .fc-timegrid-container {
    max-height: none !important; /* Remove any max-height restriction */
    overflow-y: hidden !important; /* Prevent extra scrollbar */
}
    .fc-scroller {
    overflow: hidden !important; /* Ensure no extra scrollbars appear */
}
    .fc .fc-scrollgrid-sync-table {
    position: sticky;
    top: 0;
    background: white;
    z-index: 50;
}
    /* Make the header row sticky */
.fc-theme-standard th {
    position: sticky;
    top: 40px; /* Adjust based on toolbar height */
    background: white;
    z-index: 999;
}
    .fc-view-container {
    overflow: hidden;
}


                .fc {
                    border-radius: 10px;
                    background: white;
                }
                .fc-timegrid-slot {
                    text-align: center;
                    font-size: 14px;
                    font-weight: normal;
                    padding: 5px 10px;
                     height: 40px !important; /* Increase slot height */
                     
                }
                .fc-event {
                    background-color: #007bff !important;
                    color: white !important;
                    border-radius: 5px;
                    padding: 5px;
                }
                .fc-timegrid-event {
                    font-weight: bold;
                }
                .fc-timegrid-slot-lane {
                    height: 100px;
                }
                .fc-timegrid-header {
                    font-weight: bold;
                    color: #007bff;
                }
                .fc-daygrid-day-number {
                    font-size: 16px;
                    font-weight: bold;
                    color: #007bff;
                }
                    .fc .fc-toolbar .fc-button {
                    font-size: 13px !important; /* Reduce font size */
                    margin: 5px;
                    padding: 4px 8px !important; /* Reduce padding */
                }
                .fc-toolbar-title {
                    font-size: 18px
                    font-weight: bold;
                    color: #007bff;
                }
                .fc-daygrid-header {
                    font-weight: bold;
                    color: #007bff;
                    font-size: 18px;
                }
                .fc-button {
                    background-color: #007bff;
                    color: white;
                    border-radius: 5px;
                    padding: 8px 15px;
                }
                .fc-button:hover {
                    background-color: #0056b3;
                }
            `}</style>

             {/* Modal for Appointment Details */}
            <Modal show={showModal} onHide={() => setShowModal(false)} centered>
                <Modal.Header closeButton>
                    <Modal.Title>Appointment Request</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    {selectedAppointment && (
                        <>
                            <p><strong>NIC:</strong> {selectedAppointment.title.replace("Request from ", "")}</p>
                            <p><strong>Start Time:</strong> {selectedAppointment.start.toLocaleString()}</p>
                            <p><strong>End Time:</strong> {selectedAppointment.end.toLocaleString()}</p>
                            <p><strong>Status:</strong> {selectedAppointment.status}</p>
                        </>
                    )}
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="success" onClick={handleAccept}>Accept</Button>
                    <Button variant="danger" onClick={handleReject}>Reject</Button>
                </Modal.Footer>
            </Modal>
            </>
        )
}