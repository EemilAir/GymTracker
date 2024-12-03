import React from 'react';
import styled from 'styled-components';
import axios from './axiosConfig';

const LogCard = styled.div`
  width: 100%;
  max-width: 200px; /* Adjust as needed */
  background-color: #f9f9f9;
  border: 1px solid #ddd;
  border-radius: 8px;
  padding: 15px;
  margin-bottom: 10px;
  margin-right: 10px; /* Add margin between cards */
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  align-items: center;
`;

const LogDetails = styled.div`
  flex-grow: 1;
`;

const DeleteButton = styled.button`
  padding: 8px 12px;
  background-color: #dc3545;
  border: none;
  border-radius: 5px;
  color: #ffffff;
  cursor: pointer;
  font-size: 0.9rem;

  &:hover {
    background-color: #c82333;
  }

  &:focus {
    outline: 2px dashed #dc3545;
    outline-offset: 4px;
  }
`;

const SetsRow = styled.div`
  display: flex;
  flex-wrap: wrap; /* Allows wrapping to next line if necessary */
  margin-bottom: 20px; /* Space between date groups */
`;

// The ExerciseLogs component displays the logs for a specific exercise.
const ExerciseLogs = ({ exerciseId, groupedLogs, setLogsUpdated }) => {
  
  // The handleDeleteLog function sends a DELETE request to the server to delete a log.
  const handleDeleteLog = async (logId) => {
    try {
      console.log('exerciseId:', exerciseId, 'logId:', logId);
      await axios.delete(`/exercises/${exerciseId}/logs/${logId}`);
      setLogsUpdated((prev) => !prev); // Refreshes the logs
    } catch (error) {
      console.error('Error deleting log:', error);
      alert('Failed to delete log. Please try again.');
    }
  };

  return (
    <div>
      {groupedLogs.map(({ date, sets }) => (
        <div key={date}>
          <h3>{date}</h3>
          <SetsRow>
          {sets.map((log) => (
            <LogCard key={log.id}>
              <LogDetails>
                <p>Weight: {log.weight} kg</p>
                <p>Reps: {log.reps}</p>
              </LogDetails>
              <DeleteButton onClick={() => handleDeleteLog(log.id)}>
                Delete
              </DeleteButton>
            </LogCard>
          ))}
          </SetsRow>
        </div>
      ))}
    </div>
  );
};

export default ExerciseLogs;
