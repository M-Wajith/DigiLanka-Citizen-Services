import React, { useEffect, useState ,useRef} from "react";
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap-icons/font/bootstrap-icons.css';
import { useNavigate } from 'react-router-dom';
import { Navbar, Nav, Container, Dropdown, Form, Button } from 'react-bootstrap';
import axios from "axios";
import Webcam from "react-webcam"; // Import React webcam component

export default function EditCitizen() {
    const [activeLink, setActiveLink] = useState("/edit-citizen");
    const [loading, setLoading] = useState(false);
    const [citizenData, setCitizenData] = useState(null);
    const [error, setError] = useState(null);
    const [step, setStep] = useState(1);
  
    // Step 1 Fields
    const [birthCertificateNumber, setBirthCertificateNumber] = useState("");
    const [birthDate, setBirthDate] = useState("");
    const [gender, setGender] = useState("");
    const [formErrors, setFormErrors] = useState({});
  
    // Step 2 Fields
    const [capturedFaceImage, setCapturedFaceImage] = useState(null);
    const [files, setFiles] = useState({
      birthCertificateFront: null,
      birthCertificateBack: null,
    });
    const [fileErrors, setFileErrors] = useState({});
  
    const navigate = useNavigate();
    const webcamRef = useRef(null); // ✅ Moved outside functions
  
    useEffect(() => {
      setActiveLink("/edit-citizen");
    }, []);

    const handleButtonClick = () => {
        setLoading(true); // Set loading state to true
        setTimeout(() => {
            navigate('/add-citizen'); // Navigate to the Add Citizen page
        }, 500); // Adjust the timeout duration as needed
    };
    const handleEditButtonClick = () => {
        setLoading(true); // Set loading state to true
        setTimeout(() => {
            navigate('/edit-citizen'); // Navigate to the Edit Citizen page
        }, 500); // Adjust the timeout duration as needed
    };
    const handleHomeButtonClick = () => {
        setLoading(true); // Set loading state to true
        setTimeout(() => {
            navigate('/divisional-dashboard'); // Navigate to the Home page
        }, 500); // Adjust the timeout duration as needed
    };
    
    // Handle logout
    const handleLogout = () => {
        // Implement logout logic here
        console.log("Logging out...");
        // Redirect to login page or handle session end
        navigate('/login');
    };
  
    const handleCheck = async () => {
      try {
        const response = await axios.get(`http://localhost:5000/get_citizen/${birthCertificateNumber}`);
        setCitizenData(response.data);
        setError(null);
      } catch (err) {
        setError("Citizen not found. Please enter a valid number.");
        setCitizenData(null);
      }
    };
  
    const handleNext = () => {
      if (validateStep1()) {
        setStep(2);
      }
    };
    // Step 1 Validation
const validateStep1 = () => {
    let errors = {};
  
    if (!birthCertificateNumber) errors.birthCertificateNumber = "Required";
    if (!birthDate) errors.birthDate = "Required";
    if (!gender) errors.gender = "Required";
    if (!citizenData?.first_name) errors.first_name = "Required";
    if (!citizenData?.last_name) errors.last_name = "Required";
    if (!citizenData?.nic_number) errors.nic_number = "Required";
    if (!citizenData?.address) errors.address = "Required";
  
    setFormErrors(errors);
    return Object.keys(errors).length === 0;
  };
  
  // Step 2 Validation
  const validateStep2 = () => {
    let errors = {};

    console.log("Files State:", files); // Debugging log

    if (!files.birthCertificateFront) errors.birthCertificateFront = "Required";
    if (!files.birthCertificateBack) errors.birthCertificateBack = "Required";
    if (!capturedFaceImage) errors.capturedFaceImage = "Required";

    setFileErrors(errors);
    return Object.keys(errors).length === 0;
};

    const handleBack = () => {
      setStep(1);
    };

  
    const handleUpdate = async () => {
        if (validateStep2()) {
            try {
                const formData = new FormData();
    
                formData.append("birth_certificate_number", citizenData?.birth_certificate_number || "");
                formData.append("first_name", citizenData?.first_name || "");
                formData.append("middle_name", citizenData?.middle_name || "");
                formData.append("last_name", citizenData?.last_name || "");
                formData.append("date_of_birth", birthDate);
                formData.append("gender", gender.charAt(0).toUpperCase() + gender.slice(1)); // Fix case
                formData.append("nic_number", citizenData?.nic_number || "");
                formData.append("permanent_address", citizenData?.permanent_address || "");
    
                if (!capturedFaceImage) {
                    alert("Captured face image is required");
                    return;
                }
                formData.append("captured_face_image", capturedFaceImage);
    
                if (files.birthCertificateFront) {
                    formData.append("birth_certificate_front", files.birthCertificateFront);
                }
                if (files.birthCertificateBack) {
                    formData.append("birth_certificate_back", files.birthCertificateBack);
                }
    
                console.log("=== Sending Data to Backend ===");
                for (let pair of formData.entries()) {
                    console.log(pair[0], "=>", pair[1]);
                }
    
                const response = await axios.post("http://localhost:5000/update_citizen", formData, {
                    headers: { "Content-Type": "multipart/form-data" },
                });
    
                if (response.status === 200) {
                    alert("Citizen data updated successfully!");
                    navigate("/divisional-dashboard");
                } else {
                    alert("Failed to update citizen data.");
                }
            } catch (error) {
                console.error("Error updating citizen data:", error);
                alert("An error occurred while updating the citizen.");
            }
        }
    };
    
      
  
    const handleInputChange = (e) => {
      const { name, value } = e.target;
      setCitizenData((prevData) => ({
        ...prevData,
        [name]: value,
      }));
    };
  
    const handleFileUpload = (e) => {
        const { name, files } = e.target;
        if (files.length > 0) {
            setFiles((prevFiles) => ({
                ...prevFiles,
                [name === "birth_certificate_front" ? "birthCertificateFront" : "birthCertificateBack"]: files[0],
            }));
        }
    };
    
  
    const handleCaptureFace = () => {
        const imageSrc = webcamRef.current.getScreenshot();
     
        if (imageSrc) {
          const img = new Image();
          img.src = imageSrc;
     
          img.onload = () => {
            const canvas = document.createElement("canvas");
            const ctx = canvas.getContext("2d");
     
            canvas.width = img.width;
            canvas.height = img.height;
     
            ctx.drawImage(img, 0, 0);
     
            const capturedImageSrc = canvas.toDataURL("image/jpeg");
            setCapturedFaceImage(capturedImageSrc);
     
            console.log("Captured face image:", capturedImageSrc); // Log the captured image to verify
          };
        }
     }
     
 
  

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
                className="text-light"
                style={{ fontSize: '16px', padding: '10px 15px' }}
                onClick={handleHomeButtonClick}
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
                className="text-light d-flex align-items-center"
                style={{
                  fontSize: '16px',
                  padding: '10px 15px',
                  color: '#007bff', // Primary color text for active
                  textDecoration: 'underline', // Underline effect for active link
                  fontWeight: 'bold', // Optional: Make text bold
                  marginRight: '5px', // Spacing between nav items
                }}
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
        <h1 style={{ display: 'inline-block', borderBottom: '2px solid #007bff', paddingBottom: '10px' }}>Edit Citizen</h1>
        <div className="container-fluid mt-4 d-flex justify-content-center" style={{ minHeight: '50vh' }}>
          <div className="shadow-lg rounded bg-light p-4 d-flex flex-column align-items-center" style={{ width: '90%', maxWidth: '800px', minHeight: '65vh', boxShadow: '0 10px 30px rgba(0, 0, 0, 0.2)' }}>
           

            {/* Step 1: Personal Info */}
            {step === 1 && !citizenData && (
              <Form className="w-100 text-center"> encType="multipart/form-data"
                <Form.Group className="mb-3">
                  <Form.Label style={{ fontSize: '18px', fontWeight: 'bold' }}>Enter Birth Certificate Number</Form.Label>
                  <Form.Control
                    type="text"
                    placeholder="Enter number"
                    value={birthCertificateNumber}
                    onChange={(e) => setBirthCertificateNumber(e.target.value)}
                  />
                </Form.Group>
                <Button variant="primary" onClick={handleCheck}>Check</Button>
              </Form>
            )}

            {error && <p className="text-danger mt-3">{error}</p>}

            {citizenData && step === 1 && (
              <>
                <h3 className="text-center mb-4">Personal Info</h3>
                <Form className="w-100" encType="multipart/form-data">
                  <div className="row mb-3">
                    <div className="col">
                      <Form.Control
                        type="text"
                        name="first_name"
                        value={citizenData.first_name}
                        onChange={handleInputChange}
                        placeholder="First Name"
                        isInvalid={formErrors.first_name} 
                      />
                      {formErrors.first_name && (
                        <Form.Control.Feedback type="invalid" className="text-danger">
                        {formErrors.first_name}
                        </Form.Control.Feedback>
                    )}
                    </div>
                    <div className="col">
                      <Form.Control
                        type="text"
                        name="middle_name"
                        value={citizenData.middle_name}
                        onChange={handleInputChange}
                        placeholder="Middle Name"
                        
                      />
                    </div>
                  </div>

                  <div className="row mb-3">
                    <div className="col">
                      <Form.Control
                        type="text"
                        name="last_name"
                        value={citizenData.last_name}
                        onChange={handleInputChange}
                        placeholder="Last Name"
                        isInvalid={formErrors.last_name}
                      />
                      {formErrors.last_name && (
                            <Form.Control.Feedback type="invalid" className="text-danger">
                            {formErrors.last_name}
                            </Form.Control.Feedback>
                        )}
                    </div>
                    <div className="col">
                      <Form.Control
                        type="date"
                        name="birth_date"
                        value={birthDate}
                        onChange={(e) => setBirthDate(e.target.value)}
                        placeholder="Birth Date"
                        isInvalid={formErrors.birthDate}
                      />
                      {formErrors.birthDate && (
                            <Form.Control.Feedback type="invalid" className="text-danger">
                            {formErrors.birthDate}
                            </Form.Control.Feedback>
                        )}
                    </div>
                  </div>

                  <div className="row mb-3">
                    <div className="col">
                      <Form.Control
                        as="select"
                        name="gender"
                        value={gender}
                        onChange={(e) => setGender(e.target.value)}
                        placeholder="Gender"
                        isInvalid={formErrors.gender}
                      >
                        {formErrors.gender && (
                            <Form.Control.Feedback type="invalid" className="text-danger">
                            {formErrors.gender}
                            </Form.Control.Feedback>
                        )}
                        <option value="">Select Gender</option>
                        <option value="male">Male</option>
                        <option value="female">Female</option>
                        <option value="other">Other</option>
                      </Form.Control>
                    </div>
                    <div className="col">
                      <Form.Control
                        type="text"
                        name="nic_number"
                        value={citizenData.nic_number}
                        onChange={handleInputChange}
                        placeholder="NIC Number"
                        isInvalid={formErrors.nic_number}
                      />
                      {formErrors.gender && (
                            <Form.Control.Feedback type="invalid" className="text-danger">
                            {formErrors.nic_number}
                            </Form.Control.Feedback>
                        )}
                    </div>
                  </div>

                  <div className="row mb-3">
                    <div className="col">
                      <Form.Control
                        type="text"
                        name="address"
                        value={citizenData.address}
                        onChange={handleInputChange}
                        placeholder="Address"
                        isInvalid={formErrors.address}
                      />
                      {formErrors.address && (
                            <Form.Control.Feedback type="invalid" className="text-danger">
                            {formErrors.address}
                            </Form.Control.Feedback>
                        )}
                    </div>
                  </div>

                  <Button variant="primary" onClick={handleNext}>Next Step</Button>
                </Form>
              </>
            )}

            {/* Step 2: File Upload & Webcam */}
            {/* Step 2: File Upload & Webcam */}
            {step === 2 && (
                <>
                    <h3 className="text-center mb-4">Upload Documents and Capture Face Image</h3>
                    <Form className="w-100" encType="multipart/form-data">
                        {/* File inputs */}
                        <div className="mb-3">
                            <Form.Label>Upload Birth Certificate Front</Form.Label>
                            <Form.Control 
                                type="file" 
                                name="birth_certificate_front" 
                                onChange={handleFileUpload} 
                                isInvalid={fileErrors.birthCertificateFront} 
                            />
                            {fileErrors.birthCertificateFront && (
                                <Form.Control.Feedback type="invalid" className="text-danger">
                                    {fileErrors.birthCertificateFront}
                                </Form.Control.Feedback>
                            )}
                        </div>
                        <div className="mb-3">
                            <Form.Label>Upload Birth Certificate Back</Form.Label>
                            <Form.Control 
                                type="file" 
                                name="birth_certificate_back" 
                                onChange={handleFileUpload}  
                                isInvalid={fileErrors.birthCertificateBack} 
                            />
                            {fileErrors.birthCertificateBack && (
                                <Form.Control.Feedback type="invalid" className="text-danger">
                                    {fileErrors.birthCertificateBack}
                                </Form.Control.Feedback>
                            )}
                        </div>

                        {/* Webcam capture */}
                        <div className="mb-3 text-center">
                            <Form.Label className="d-block mb-2" style={{ fontSize: '18px', fontWeight: 'bold' }}>
                                Capture Face Image
                            </Form.Label>
                            <div className="d-flex flex-column align-items-center">
                                {/* Wrapper div around webcam */}
                                <Form.Control 
                                    as="div" 
                                    isInvalid={fileErrors.capturedFaceImage} 
                                    className="mb-2"
                                >
                                    <Webcam
                                        audio={false}
                                        ref={webcamRef}
                                        screenshotFormat="image/jpeg"
                                        width={320} // Set a fixed size for consistency
                                        height={240}
                                        className="border rounded"
                                    />
                                </Form.Control>
                                <Button variant="secondary" onClick={handleCaptureFace}>Capture Face</Button>

                                {/* Show error message if capturedFaceImage is null or undefined */}
                                {fileErrors.capturedFaceImage && (
                                    <Form.Control.Feedback type="invalid" className="text-danger">
                                        {fileErrors.capturedFaceImage}
                                    </Form.Control.Feedback>
                                )}
                            </div>
                            {capturedFaceImage && (
                                <div className="mt-3">
                                    <img src={capturedFaceImage} alt="Captured Face" className="border rounded" style={{ maxWidth: '150px' }} />
                                </div>
                            )}
                        </div>

                        <div className="d-flex justify-content-between">
                            <Button variant="secondary" onClick={handleBack}>Back</Button>
                            <Button variant="primary" onClick={handleUpdate}>Update</Button>
                        </div>
                    </Form>
                </>
            )}



          </div>
        </div>
      </div>
    </>
  );
}
