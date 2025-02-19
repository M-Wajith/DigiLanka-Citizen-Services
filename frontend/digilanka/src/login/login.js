import React, { useState } from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import { useNavigate } from 'react-router-dom';

export default function Login() {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const navigate = useNavigate();

    const handleSubmit = async (event) => {
        event.preventDefault();

        // Enhanced client-side validation
        if (!username && !password) {
            setError('Please enter both username and password');
            return;
        }
        if (!username) {
            setError('Please enter your username');
            return;
        }
        if (!password) {
            setError('Please enter your password');
            return;
        }
    
        try {
            const response = await fetch('http://127.0.0.1:5000/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ username, password }),
            });
    
            const data = await response.json();
    
            if (response.ok) {
                navigate(data.redirect);
            } else {
                setError(data.error || 'An error occurred');
            }
        } catch (error) {
            console.error('Error:', error);
            setError('An error occurred');
        }
    };

    return (
        <>
            <div className="container-fluid">
                <div className="row">
                    {/* Left side with grey background */}
                    <div className="col-md-6 bg-secondary d-flex flex-column justify-content-center align-items-center" style={{ height: '100vh' }}>
                        <div className="text-center">
                            <img src="Digi_Goverment.png" alt="Digi Government" style={{ maxWidth: '100%', height: 'auto' }} /> <br />
                            <img src="heading.png" alt="Heading" style={{ maxWidth: '100%', height: 'auto', marginTop: '0' }} />
                        </div>
                    </div>
                    {/* Right side with login form */}
                    <div className="col-md-6 d-flex justify-content-center align-items-center" style={{ height: '100vh' }}>
                        <div className="p-4 w-100" style={{ maxWidth: '400px' }}>
                            <div className="text-center mb-4">
                                <h1 style={{ borderBottom: '2px solid #276BF0', display: 'inline-block' }}>Login</h1>
                            </div>
                            <form onSubmit={handleSubmit}>
                                <div className="mb-3">
                                    <input
                                        type="text"
                                        className="form-control"
                                        placeholder="Username"
                                        value={username}
                                        onChange={(e) => setUsername(e.target.value)}
                                    />
                                </div>
                                <div className="mb-3">
                                    <input
                                        type="password"
                                        className="form-control"
                                        placeholder="Password"
                                        value={password}
                                        onChange={(e) => setPassword(e.target.value)}
                                    />
                                </div>
                                <button type="submit" className="btn btn-primary w-100">Login</button>
                                {error && <div className="text-danger mt-3 text-center">{error}</div>}
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </>
    );
}
