import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import Login from './login/login';  // Ensure the path is correct
import DashboardDS from './Divisional/DashboardDS';  // Ensure this path is correct
import AddCitizen from './Divisional/Addcitizen';  // Ensure this path matches the file name exactly
import EditCitizen from './Divisional/Editcitizen';
import Newpassport from './Passport/Newpassport';
import Newnic from './Nic/Newnic';
import Writtenexam from './License/Wriitenexam';
import Verifiedpassport from './Passport/Verifiedpassport';
import Printedpassport from './Passport/Printedpassport';
import Dispatchedpassport from './Passport/Dispatchedpassport';
import Medicalexam from './License/medicalexam';
import Practicalexam from './License/practicalexam';
import Verifiednic from './Nic/Verifiednic';
import Printednic from './Nic/Printednic';
import Dispatchnic from './Nic/Dispatchednic';


function App() {
    return (
        <Router>
            <Routes>
                <Route path="/login" element={<Login />} />
                <Route path="/divisional-dashboard" element={<DashboardDS />} />
                <Route path="/add-citizen" element={<AddCitizen />} />
                <Route path="/edit-citizen" element={<EditCitizen />} />
                <Route path="/passport-dashboard" element={<Newpassport />} />
                <Route path="/nic-dashboard" element={<Newnic />} />
                <Route path="/verified-nic" element={<Verifiednic />} />
                <Route path="/Printed-nic" element={<Printednic />} />
                <Route path="/dispatched-nic" element={<Dispatchnic />} />
                <Route path="/license-dashboard" element={<Medicalexam />} />
                <Route path="/written" element={<Writtenexam />} />
                <Route path="/practical" element={<Practicalexam />} />
                <Route path="/verified-passport" element={<Verifiedpassport />} />
                <Route path="/printed-passport" element={<Printedpassport />} />
                <Route path="/dispatched-passport" element={<Dispatchedpassport />} />

                {/* Add other routes here */}
                <Route path="/" element={<Login />} />
            </Routes>
        </Router>
    );
}

export default App;
