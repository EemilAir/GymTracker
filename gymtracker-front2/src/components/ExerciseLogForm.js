import React, { useState, useEffect } from 'react';
import styled from 'styled-components';
import axios from './axiosConfig';

const Card = styled.div`
  background-color: #ffffff;
  padding: 30px;
  border: 1px solid #ddd;
  border-radius: 15px;
  width: 300px;
  height: 300px;
  margin: 40px auto;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
`;

const Form = styled.form`
  display: flex;
  flex-direction: column;
  gap: 15px;
`;

const InputContainer = styled.div`
  display: flex;
  align-items: center;
  justify-content: center; /* Centering the input and buttons */
`;

const AdjustButton = styled.button`
  padding: 15px 20px; /* Increased size */
  background-color: #e0e0e0;
  border: none;
  border-radius: 8px; /* More rounded */
  cursor: pointer;
  font-size: 1.5rem; /* Larger icons */

  &:hover {
    background-color: #008000;
  }
`;

const NumberInput = styled.input`
  width: 150px; /* Increased width */
  height: 100px; /* Increased height */
  text-align: center;
  font-size: 4.5rem; /* Larger numbers */
  margin: 0 10px;
  border: 2px solid #ccc;
  border-radius: 8px;

  &:focus {
    border-color: #17a2b8;
    outline: none;
  }
`;

const Button = styled.button`
  padding: 12px 20px;
  background-color: #17a2b8;
  border: none;
  border-radius: 8px;
  color: #ffffff;
  cursor: pointer;
  font-size: 1.5rem;

  &:hover {
    background-color: #138496;
  }

  &:focus {
    outline: 2px dashed #17a2b8;
    outline-offset: 4px;
  }
`;

const ErrorMessage = styled.p`
  color: #dc3545;
  font-size: 0.9rem;
  text-align: center;
`;

const SuccessMessage = styled.p`
  color: #28a745;
  font-size: 0.9rem;
  text-align: center;
`;

// The ExerciseLogForm component is a form that allows users to log their exercise details.
const ExerciseLogForm = ({ exerciseId, onLogAdded, lastLog }) => {
  const [step, setStep] = useState('weight'); // 'weight' or 'reps' switch between steps
  const [weight, setWeight] = useState('');
  const [reps, setReps] = useState('');
  
  const [error, setError] = useState('');

  // Sets initial values if lastLog is available
  useEffect(() => {
    if (lastLog) {
      setWeight(lastLog.weight);
      setReps(lastLog.reps);
    }
  }, [lastLog]);

  // Handles the submission of the weight form
  const handleWeightSubmit = (e) => {
    e.preventDefault();
    if (weight === '' || weight < 0) {
      setError('Valid weight is required.');
      return;
    }
    setError('');
    setStep('reps');
  };

  //  Handles the submission of the reps form and sends the log to the server
  const handleRepsSubmit = async (e) => {
    e.preventDefault();
    if (reps === '' || reps < 0) {
      setError('Valid repetitions are required.');
      return;
    }
    setError('');

    try {
      await axios.post(`/exercises/${exerciseId}/logs`, { weight, reps });
      onLogAdded();
      // Reset form
      setWeight(lastLog ? lastLog.weight : '');
      setReps(lastLog ? lastLog.reps : '');
      setStep('weight');
    } catch (err) {
      setError('Failed to add log. Please try again.');
    }
  };

  // Adjusts the weight or reps value based on the operation
  const adjustValue = (type, operation) => {
    if (type === 'weight') {
      setWeight(prev => {
        const newVal = operation === 'increase' ? Number(prev) + 1 : Number(prev) - 1;
        return newVal >= 0 ? newVal : 0;
      });
    } else if (type === 'reps') {
      setReps(prev => {
        const newVal = operation === 'increase' ? Number(prev) + 1 : Number(prev) - 1;
        return newVal >= 0 ? newVal : 0;
      });
    }
  };

  return (
    <Card>
      {step === 'weight' && (
        <>
          <h1>Step 1</h1>
          <h3>Add Weight</h3>
          <Form onSubmit={handleWeightSubmit}>
            <InputContainer>
              <AdjustButton type="button" onClick={() => adjustValue('weight', 'decrease')}>
                {'<'}
              </AdjustButton>
              <NumberInput
                type="number"
                placeholder="0"
                value={weight}
                onChange={(e) => setWeight(e.target.value)}
                required
                min="0"
              />
              <AdjustButton type="button" onClick={() => adjustValue('weight', 'increase')}>
                {'>'}
              </AdjustButton>
            </InputContainer>
            {error && <ErrorMessage>{error}</ErrorMessage>}
            <Button type="submit">Next</Button>
          </Form>
        </>
      )}

      {step === 'reps' && (
        <>
          <h1>Step 2</h1>
          <h3>Add Reps</h3>
          <Form onSubmit={handleRepsSubmit}>
            <InputContainer>
              <AdjustButton type="button" onClick={() => adjustValue('reps', 'decrease')}>
                {'<'}
              </AdjustButton>
              <NumberInput
                type="number"
                placeholder="0"
                value={reps}
                onChange={(e) => setReps(e.target.value)}
                required
                min="0"
              />
              <AdjustButton type="button" onClick={() => adjustValue('reps', 'increase')}>
                {'>'}
              </AdjustButton>
            </InputContainer>
            {error && <ErrorMessage>{error}</ErrorMessage>}
            <Button type="submit">Add Rep</Button>
          </Form>
        </>
      )}
    </Card>
  );
};

export default ExerciseLogForm;