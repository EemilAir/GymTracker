import React, { useState, useContext } from 'react';
import styled from 'styled-components';
import axios from '../components/axiosConfig';
import { useNavigate, Link } from 'react-router-dom';
import { AuthContext } from '../context/AuthContext';

const LoginContainer = styled.div`
  max-width: 400px;
  margin: 50px auto;
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
    border-color: #007BFF;
    outline: none;
  }
`;

const Button = styled.button`
  padding: 12px;
  background-color: #007BFF;
  border: none;
  border-radius: 5px;
  color: #ffffff;
  font-size: 1rem;
  cursor: pointer;
  transition: background-color 0.3s ease;

  &:hover {
    background-color: #0056b3;
  }

  &:focus {
    outline: 2px dashed #0056b3;
    outline-offset: 4px;
  }
`;

const ErrorMessage = styled.p`
  color: red;
  font-size: 0.9rem;
`;

// The Login component allows users to log in to the application.
const Login = () => {
  const navigate = useNavigate();
  const { login } = useContext(AuthContext); // Consume AuthContext
  const [username, setUsername] = useState('');
  const [passwordHash, setPasswordHash] = useState('');
  const [error, setError] = useState('');

  const handleLogin = async (e) => {
    e.preventDefault();
    setError('');
    
    try {
      const response = await axios.post('/login', { username, passwordHash }); 
      console.log('Login Response:', response.data); 

      const { token, id, username: returnedUsername } = response.data; // Extract 'token' and 'id'

      if (token && id && returnedUsername) {
        login(token, id, returnedUsername); // Store username in localStorage
        navigate('/'); // Redirect to home or desired page
      } else {
        throw new Error('Authentication data missing');
      }
    } catch (err) {
      console.error('Login failed:', err);
      if (err.response && err.response.status === 401) {
        setError('Invalid username or password. Please try again.');
      } else {
        setError('An error occurred. Please try again later.');
      }
    }
  };

  return (
    <LoginContainer>
      <h2>Login</h2>
      <Form onSubmit={handleLogin}>
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
          value={passwordHash}
          onChange={(e) => setPasswordHash(e.target.value)}
          aria-label="Password"
          required
        />
        {error && <ErrorMessage>{error}</ErrorMessage>}
        <Button type="submit">Login</Button>
      </Form>
      <p>
        Don't have an account? <Link to="/register">Register Here</Link>
      </p>
    </LoginContainer>
  );
};

export default Login;