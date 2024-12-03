import React, { useState } from 'react';
import styled from 'styled-components';
import axios from './axiosConfig';

const FormContainer = styled.div`
  background-color: #f9f9f9;
  padding: 20px;
  border: 1px solid #ddd;
  border-radius: 5px;
  max-width: 400px; /* Set a maximum width */
  margin: 20px; /* Add margin to move it left */

  h3 {
    margin-bottom: 15px;
  }
`;

const Input = styled.input`
  width: 100%;
  padding: 10px;
  margin-bottom: 10px;
  border: 1px solid #ccc;
  border-radius: 4px;
`;

const Button = styled.button`
  padding: 10px 15px;
  background-color: #28a745;
  border: none;
  color: white;
  border-radius: 4px;
  cursor: pointer;
  
  &:hover {
    background-color: #218838;
  }
`;

const ErrorMessage = styled.p`
  color: red;
`;

// The ExerciseForm component is used to add a new exercise to the user's list of exercises.
const ExerciseForm = ({ userId, onExerciseAdded }) => {
  const [exerciseName, setExerciseName] = useState('');
  const [description, setDescription] = useState('');
  const [error, setError] = useState('');

  const handleAddExercise = async (e) => {
    e.preventDefault();
    setError('');

    if (!exerciseName.trim()) {
      setError('Exercise name is required.');
      return;
    }

    try {
      const response = await axios.post(`/users/${userId}/exercises`, { name: exerciseName, description });
      console.log('Exercise Created:', response.data);
      
      // Reset form fields
      setExerciseName('');
      setDescription('');
      
      // Call the callback to notify parent component
      if (onExerciseAdded) {
        console.log('Calling onExerciseAdded callback'); // Debug log
        onExerciseAdded();
      }
    } catch (err) {
      console.error('Failed to add exercise:', err);
      setError('Failed to add exercise. Please try again.');
    }
  };

  return (
    <FormContainer>
      <h3>Add New Exercise</h3>
      <form onSubmit={handleAddExercise}>
        <Input
          type="text"
          placeholder="Exercise Name"
          value={exerciseName}
          onChange={(e) => setExerciseName(e.target.value)}
          required
        />
        <Input
          type="text"
          placeholder="Description"
          value={description}
          onChange={(e) => setDescription(e.target.value)}
        />
        {error && <ErrorMessage>{error}</ErrorMessage>}
        <Button type="submit">Add Exercise</Button>
      </form>
    </FormContainer>
  );
};

export default ExerciseForm;