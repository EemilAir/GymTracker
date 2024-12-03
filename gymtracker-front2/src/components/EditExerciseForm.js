import React, { useState } from 'react';
import styled from 'styled-components';

const Form = styled.form`
  display: flex;
  flex-direction: column;
  gap: 15px;
`;

const Label = styled.label`
  font-weight: bold;
  margin-bottom: 5px;
`;

const Input = styled.input`
  padding: 10px;
  border: 1px solid #cccccc;
  border-radius: 5px;
  font-size: 1rem;
`;

const TextArea = styled.textarea`
  padding: 10px;
  border: 1px solid #cccccc;
  border-radius: 5px;
  font-size: 1rem;
  resize: vertical;
`;

const ButtonGroup = styled.div`
  display: flex;
  gap: 10px;
`;

const SubmitButton = styled.button`
  padding: 10px 15px;
  background-color: #28a745;
  border: none;
  border-radius: 5px;
  color: #ffffff;
  cursor: pointer;
  font-size: 1rem;

  &:hover {
    background-color: #218838;
  }

  &:focus {
    outline: 2px dashed #28a745;
    outline-offset: 4px;
  }
`;

const CancelButton = styled.button`
  padding: 10px 15px;
  background-color: #6c757d;
  border: none;
  border-radius: 5px;
  color: #ffffff;
  cursor: pointer;
  font-size: 1rem;

  &:hover {
    background-color: #5a6268;
  }

  &:focus {
    outline: 2px dashed #6c757d;
    outline-offset: 4px;
  }
`;

const ErrorMessage = styled.p`
  color: #dc3545;
  font-size: 0.9rem;
  text-align: center;
`;

/* The EditExerciseForm component is a form that allows users to edit an exercise. 
It displays the current exercise name and description as input fields. When the form is submitted, it calls 
the onSubmit function with the updated exercise data. */
const EditExerciseForm = ({ exercise, onSubmit, onCancel }) => {
  const [name, setName] = useState(exercise.name);
  const [description, setDescription] = useState(exercise.description);
  const [error, setError] = useState('');

  const handleSubmit = (e) => {
    e.preventDefault();
    if (!name.trim()) {
      setError('Exercise name is required.');
      return;
    }

    // Prepare updated exercise data
    const updatedExercise = {
      id: exercise.id,
      name: name.trim(),
      description: description.trim(),
    };
    
    onSubmit(updatedExercise);
  };

  return (
    <Form onSubmit={handleSubmit}>
      <div>
        <Label htmlFor="exerciseName">Exercise Name:</Label>
        <Input
          id="exerciseName"
          type="text"
          value={name}
          onChange={(e) => setName(e.target.value)}
          required
        />
      </div>

      <div>
        <Label htmlFor="exerciseDescription">Description:</Label>
        <TextArea
          id="exerciseDescription"
          rows="4"
          value={description}
          onChange={(e) => setDescription(e.target.value)}
        />
      </div>

      {error && <ErrorMessage>{error}</ErrorMessage>}

      <ButtonGroup>
        <SubmitButton type="submit">Save</SubmitButton>
        <CancelButton type="button" onClick={onCancel}>
          Cancel
        </CancelButton>
      </ButtonGroup>
    </Form>
  );
};

export default EditExerciseForm;