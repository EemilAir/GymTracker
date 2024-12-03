import React, { useState } from 'react';
import styled from 'styled-components';
import axios from '../components/axiosConfig';
import { useNavigate, Link } from 'react-router-dom';
import { API_BASE_URL } from './config';

const RegisterContainer = styled.div`
  max-width: 400px;
  margin: 0 auto;
  padding: 40px;
  background-color: #ffffff;
  border-radius: 10px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
`;

const Form = styled.form`
  display: flex;
  flex-direction: column;
  gap: 20px;
`;

const Input = styled.input`
  padding: 12px;
  border: 1px solid #cccccc;
  border-radius: 5px;
  font-size: 1rem;

  &:focus {
    border-color: #28a745;
    outline: none;
  }
`;

const Button = styled.button`
  padding: 12px;
  background-color: #28a745;
  border: none;
  border-radius: 5px;
  color: #ffffff;
  font-size: 1rem;
  cursor: pointer;
  transition: background-color 0.3s ease;

  &:hover {
    background-color: #218838;
  }

  &:focus {
    outline: 2px dashed #218838;
    outline-offset: 4px;
  }
`;

const ErrorMessage = styled.p`
  color: #dc3545; /* Bootstrap danger color */
  background-color: #f8d7da; /* Light red background */
  border: 1px solid #f5c6cb; /* Border matching the background */
  border-radius: 4px;
  padding: 10px;
  margin-top: 10px;
  font-size: 0.9rem;
`;

const Register = () => {
  const navigate = useNavigate();
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [error, setError] = useState('');

  const handleRegister = async (e) => {
    e.preventDefault();
    setError('');

    // Validate password match before sending request to server
    if (password !== confirmPassword) {
      setError('Passwords do not match.');
      return;
    }

    try {
      await axios.post(`${API_BASE_URL}/register`, { username, passwordHash: password });
      // Redirect to Login page after successful registration
      navigate('/login');
    } catch (err) {
      console.error('Registration failed:', err);
  
      if (err.response && err.response.data) {
        // Display specific error message from backend
        setError(err.response.data);
      } else {
        // Fallback for unexpected errors
        setError('Registration failed. Please try again.');
      }
    }
  };

  return (
    <RegisterContainer>
      <h2>Register</h2>
      <Form onSubmit={handleRegister}>
        <Input
          type="text"
          placeholder="Username"
          value={username}
          onChange={(e) => setUsername(e.target.value)}
          aria-label="Username"
          required
        />
        <Input
          type="password"
          placeholder="Password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          aria-label="Password"
          required
        />
        <Input
          type="password"
          placeholder="Confirm Password"
          value={confirmPassword}
          onChange={(e) => setConfirmPassword(e.target.value)}
          aria-label="Confirm Password"
          required
        />
        {error && <ErrorMessage>{error}</ErrorMessage>}
        <Button type="submit">Register</Button>
      </Form>
      <p>
        Already have an account? <Link to="/login">Login here</Link>.
      </p>
    </RegisterContainer>
  );
};

export default Register;