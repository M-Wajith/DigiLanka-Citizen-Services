import React, { useEffect, useState, useRef } from "react";
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap-icons/font/bootstrap-icons.css';
import { useNavigate } from 'react-router-dom';
import Webcam from "react-webcam"; // Import the react-webcam library
import { Navbar, Nav, Container, Dropdown } from 'react-bootstrap';

export default function AddCitizen() {
  const [activeLink, setActiveLink] = useState("/add-citizen");
   const [loading, setLoading] = useState(false);

  useEffect(() => {
    setActiveLink("/add-citizen");
  }, []);

  const [currentStep, setCurrentStep] = useState(1);
  const [formData, setFormData] = useState({
    firstName: '',
    middleName: '',
    lastName: '',
    dateOfBirth: '',
    gender: '',
    nicNumber: '',
    birthCertificateNumber: '',
    permanentAddress: '',
    birthCertificateFront: null, // Front of birth certificate
    birthCertificateBack: null,  // Back of birth certificate
    capturedFaceImage: null // Store captured face
  });

  const [errors, setErrors] = useState({});
  const navigate = useNavigate();
  const webcamRef = useRef(null); // Reference for webcam

  // Function to go to the next step
  const handleNextStep = () => {
    const validationErrors = validateStepOne();
    if (Object.keys(validationErrors).length === 0) {
      setCurrentStep(currentStep + 1);
    } else {
      setErrors(validationErrors);
    }
  };

  // Function to go back to the previous step
  const handleBackStep = () => {
    setCurrentStep(currentStep - 1);
  };
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

  // Handle input changes
  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
    setErrors({ ...errors, [name]: null }); // Clear error for the changed field
  };

  // Handle file input changes (for birth certificate front and back)
  const handleFileChange = (e) => {
    const { name, files } = e.target;
    setFormData({ ...formData, [name]: files[0] }); // Storing file objects for front and back
    setErrors({ ...errors, [name]: null }); // Clear error for the changed field
  };

  // Validate Step 1 inputs
  const validateStepOne = () => {
    const validationErrors = {};
    if (!formData.firstName) validationErrors.firstName = "First Name is required";
    if (!formData.lastName) validationErrors.lastName = "Last Name is required";
    if (!formData.dateOfBirth) validationErrors.dateOfBirth = "Date of Birth is required";
    if (!formData.gender) validationErrors.gender = "Gender is required";
    if (!formData.nicNumber) validationErrors.nicNumber = "NIC Number is required";
    if (!formData.birthCertificateNumber) validationErrors.birthCertificateNumber = "Birth Certificate Number is required";
    if (!formData.permanentAddress) validationErrors.permanentAddress = "Permanent Address is required";
    return validationErrors;
  };

  // Validate Step 2 inputs
  const validateStepTwo = () => {
    const validationErrors = {};
    if (!formData.birthCertificateFront) validationErrors.birthCertificateFront = "Birth Certificate (Front) is required";
    if (!formData.birthCertificateBack) validationErrors.birthCertificateBack = "Birth Certificate (Back) is required";
    if (!formData.capturedFaceImage) validationErrors.capturedFaceImage = "Face capture is required";
    return validationErrors;
  };

  // Capture the face using webcam and flip the captured image
  const captureFace = () => {
    const imageSrc = webcamRef.current.getScreenshot();

    if (imageSrc) {
      const img = new Image();
      img.src = imageSrc;

      img.onload = () => {
        const canvas = document.createElement('canvas');
        const ctx = canvas.getContext('2d');

        // Set canvas dimensions to match the image
        canvas.width = img.width;
        canvas.height = img.height;

        // Draw the image directly without flipping
        ctx.translate(canvas.width, 0);
        ctx.scale(-1, 1);
        ctx.drawImage(img, 0, 0);

        // Get the natural image as base64
        const capturedImageSrc = canvas.toDataURL('image/jpeg');
        setFormData({ ...formData, capturedFaceImage: capturedImageSrc });
      };
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    const validationErrors = validateStepTwo();
    if (Object.keys(validationErrors).length === 0) {
      try {
        const formDataToSubmit = new FormData();
        formDataToSubmit.append('birthCertificateFront', formData.birthCertificateFront); // Append birth certificate front
        formDataToSubmit.append('birthCertificateBack', formData.birthCertificateBack); // Append birth certificate back
        formDataToSubmit.append('capturedFaceImage', formData.capturedFaceImage); // Append captured face image

        // Append additional fields to formData
        formDataToSubmit.append('firstName', formData.firstName);
        formDataToSubmit.append('middleName', formData.middleName || '');
        formDataToSubmit.append('lastName', formData.lastName);
        formDataToSubmit.append('dateOfBirth', formData.dateOfBirth);
        formDataToSubmit.append('gender', formData.gender);
        formDataToSubmit.append('nicNumber', formData.nicNumber);
        formDataToSubmit.append('birthCertificateNumber', formData.birthCertificateNumber);
        formDataToSubmit.append('permanentAddress', formData.permanentAddress);

        console.log("Form Data to Submit:", formDataToSubmit);

        const response = await fetch('http://localhost:5000/api/citizen', {
          method: 'POST',
          body: formDataToSubmit,
          // Do NOT set Content-Type here, let the browser set the boundary for FormData
        });

        if (response.ok) {
          console.log("Form submitted successfully");
          alert("Form submitted successfully!"); // Alert message on successful submission
          navigate("/divisional-dashboard");
          // Reset the form or navigate to another page
          setFormData({
            firstName: '',
            middleName: '',
            lastName: '',
            dateOfBirth: '',
            gender: '',
            nicNumber: '',
            birthCertificateNumber: '',
            permanentAddress: '',
            birthCertificateFront: null,
            birthCertificateBack: null,
            capturedFaceImage: null
          });
          setCurrentStep(1); // Reset to first step
        } else {
          const errorText = await response.text();
          console.error("Submission failed:", errorText);
          alert("Submission failed: " + errorText); // Alert message on submission failure
        }
      } catch (error) {
        console.error("Error submitting form:", error);
        alert("Error submitting form: " + error.message); // Alert message on network error
      }
    } else {
      setErrors(validationErrors);
    }
  };

  const heading = "Add Citizen";

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
                    className="text-light d-flex align-items-center"
                            style={{
                                fontSize: '16px',
                                padding: '10px 15px',
                                color: '#007bff', // Primary color text for active
                                textDecoration: 'underline', // Underline effect for active link
                                fontWeight: 'bold', // Optional: Make text bold
                                marginRight: '5px', // Spacing between nav items
                            }}
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
      <h1 style={{ display: 'inline-block', borderBottom: '2px solid #007bff', paddingBottom: '10px' }}>
        {heading}
      </h1>

      <div className="container-fluid mt-4 d-flex justify-content-center" style={{ minHeight: '50vh' }}>
        <div className="shadow-lg rounded bg-light p-4" style={{ width: '90%', maxWidth: '800px', minHeight: '65vh', boxShadow: '0 10px 30px rgba(0, 0, 0, 0.2)' }}>
          {currentStep === 1 ? (
            // Personal Info Form
            <form>
              <h3 className="text-center mb-4">Personal Info</h3>
              <div className="row mb-3">
                <div className="col">
                  <input
                    type="text" className="form-control" placeholder="First Name" name="firstName" value={formData.firstName} onChange={handleChange} required
                  />
                  {errors.firstName && <div className="text-danger small">{errors.firstName}</div>}
                </div>
                <div className="col">
                  <input
                    type="text" className="form-control" placeholder="Middle Name" name="middleName"
                    value={formData.middleName} onChange={handleChange}
                  />
                </div>
                <div className="col">
                  <input
                    type="text" className="form-control" placeholder="Last Name" name="lastName" value={formData.lastName} onChange={handleChange} required
                  />
                  {errors.lastName && <div className="text-danger small">{errors.lastName}</div>}
                </div>
              </div>
              <div className="row mb-3">
                <div className="col">
                  <input
                    type="date" className="form-control" name="dateOfBirth"
                    value={formData.dateOfBirth} onChange={handleChange} required
                  />
                  {errors.dateOfBirth && <div className="text-danger small">{errors.dateOfBirth}</div>}
                </div>
                <div className="col">
                  <select className="form-select" name="gender" value={formData.gender} onChange={handleChange} required>
                    <option value="">Gender</option>
                    <option value="Male">Male</option>
                    <option value="Female">Female</option>
                  </select>
                  {errors.gender && <div className="text-danger small">{errors.gender}</div>}
                </div>
              </div>
              <div className="row mb-3">
                <div className="col">
                  <input
                    type="text" className="form-control" placeholder="NIC Number" name="nicNumber"
                    value={formData.nicNumber} onChange={handleChange} required
                  />
                  {errors.nicNumber && <div className="text-danger small">{errors.nicNumber}</div>}
                </div>
                <div className="col">
                  <input
                    type="text" className="form-control" placeholder="Birth Certificate Number" name="birthCertificateNumber"
                    value={formData.birthCertificateNumber} onChange={handleChange} required
                  />
                  {errors.birthCertificateNumber && <div className="text-danger small">{errors.birthCertificateNumber}</div>}
                </div>
              </div>
              <div className="mb-3">
                <textarea
                  className="form-control" placeholder="Permanent Address" name="permanentAddress"
                  value={formData.permanentAddress} onChange={handleChange} required
                ></textarea>
                {errors.permanentAddress && <div className="text-danger small">{errors.permanentAddress}</div>}
              </div>
              <div className="text-center">
                <button className="btn btn-primary" type="button" onClick={handleNextStep}>Next</button>
              </div>
            </form>
          ) : currentStep === 2 ? (
            <form>
              <h3 className="text-center mb-4">Upload Documents & Capture Face</h3>
              <div className="row mb-3">
                <div className="col">
                  <label>Upload Birth Certificate Front</label>
                  <input
                    type="file" className="form-control" name="birthCertificateFront"
                    onChange={handleFileChange} required
                  />
                  {errors.birthCertificateFront && <div className="text-danger small">{errors.birthCertificateFront}</div>}
                </div>
                <div className="col">
                  <label>Upload Birth Certificate Back</label>
                  <input
                    type="file" className="form-control" name="birthCertificateBack"
                    onChange={handleFileChange} required
                  />
                  {errors.birthCertificateBack && <div className="text-danger small">{errors.birthCertificateBack}</div>}
                </div>
              </div>
              <div className="mb-3">
                <label>Capture Your Face</label>
                <div className="text-center">
                  <Webcam
                    audio={false}
                    ref={webcamRef}
                    screenshotFormat="image/jpeg"
                    width="100%"
                    videoConstraints={{
                      facingMode: "user"
                    }}
                  />
                </div>
                <div className="text-center mt-3">
                  <button className="btn btn-secondary" type="button" onClick={captureFace}>Capture Face</button>
                  {formData.capturedFaceImage && (
                    <div className="mt-3">
                      <img src={formData.capturedFaceImage} alt="Captured Face" width="100" />
                    </div>
                  )}
                  {errors.capturedFaceImage && <div className="text-danger small">{errors.capturedFaceImage}</div>}
                </div>
              </div>
              <div className="text-center">
                <button className="btn btn-primary" type="button" onClick={handleSubmit}>Submit</button>
              </div>
            </form>
          ) : null}
        </div>
      </div>
    </div>
    </>
  );
}
