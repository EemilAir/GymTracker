import React, { useEffect, useState, useContext } from 'react';
import axios from '../components/axiosConfig';
import ExerciseForm from '../components/ExerciseForm';
import ExerciseLogForm from '../components/ExerciseLogForm';
import ExerciseLogs from '../components/ExerciseLogs';
import EditExerciseForm from '../components/EditExerciseForm';
import { useNavigate, useLocation } from 'react-router-dom';
import styled from 'styled-components';
import { AuthContext } from '../context/AuthContext';

const HomeWrapper = styled.div`
  display: flex;
  flex-direction: column;
  gap: 40px;
  padding: 20px;

  /* Responsive */
  @media (max-width: 768px) {
    gap: 20px;
    padding: 10px;
  }
`;

const EditExerciseModal = styled.div`
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
`;

const ModalContent = styled.div`
  background-color: #ffffff;
  padding: 30px;
  border-radius: 10px;
  width: 400px;
`;

const GridContainer = styled.div`
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  gap: 20px;

  @media (max-width: 768px) {
    grid-template-columns: 1fr;
  }
`;

const Card = styled.div`
  background-color: #ffffff;
  border: 1px solid #ddd;
  border-radius: 10px;
  padding: 20px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);

  display: flex;
  flex-direction: column;
  justify-content: space-between;

  transition: transform 0.2s ease, box-shadow 0.2s ease;

  &:hover {
    transform: translateY(-5px);
    box-shadow: 0 8px 12px rgba(0, 0, 0, 0.2);
  }
`;

const ButtonGroup = styled.div`
  display: flex;
  gap: 10px;
  margin-top: 10px;
`;
const EditButton = styled.button`
  padding: 8px 12px;
  background-color: #ffc107;
  border: none;
  border-radius: 5px;
  color: #ffffff;
  cursor: pointer;
  font-size: 0.9rem;

  &:hover {
    background-color: #e0a800;
  }

  &:focus {
    outline: 2px dashed #ffc107;
    outline-offset: 4px;
  }
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


const ExerciseHeader = styled.div`
  margin-bottom: 15px;
`;

const ExerciseName = styled.h3`
  margin: 0;
  color: #333;
`;

const ExerciseDescription = styled.p`
  color: #666;
  font-size: 14px;
`;

const ExerciseDetails = styled.div`
  margin-bottom: 15px;
`;

const TotalKg = styled.p`
  font-weight: bold;
  margin: 5px 0;
`;

// Styled component for displaying the difference in total kg green or red
const DiffText = styled.p`
  color: ${(props) =>
    props.variant === 'positive'
      ? 'green'
      : props.variant === 'negative'
      ? 'red'
      : 'black'};
  font-weight: bold;
  margin: 5px 0;
`;

const ViewLogsButton = styled.button`
  padding: 10px 15px;
  background-color: #007bff;
  border: none;
  border-radius: 5px;
  color: #ffffff;
  cursor: pointer;
  align-self: flex-start;
  transition: background-color 0.3s ease;

  &:hover {
    background-color: #0056b3;
  }

  &:focus {
    outline: 2px dashed #ffffff;
    outline-offset: 4px;
  }
`;

const BackButton = styled.button`
  padding: 8px 16px;
  background-color: #007bff; /* Primary color */
  border: none;
  border-radius: 5px;
  color: #ffffff;
  cursor: pointer;
  font-size: 0.9rem;
  margin-bottom: 20px; /* Optional: Adds space below the button */

  &:hover {
    background-color: #0056b3; /* Darker shade on hover */
  }

  &:focus {
    outline: 2px dashed #0056b3;
    outline-offset: 4px;
  }

  /* Optional: Add transition for smooth hover effect */
  transition: background-color 0.2s ease-in-out;
`;

// The HomePage component displays the user's exercises and logs.
const HomePage = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const { authToken, userId, username, logout } = useContext(AuthContext);

  const [exercises, setExercises] = useState([]);
  const [logsGrouped, setLogsGrouped] = useState({});
  const [logsUpdated, setLogsUpdated] = useState(false);
  const [selectedExerciseId, setSelectedExerciseId] = useState(null);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState('');

  // State for editing
  const [isEditing, setIsEditing] = useState(false);
  const [exerciseToEdit, setExerciseToEdit] = useState(null);

  // Function to fetch exercises
  const fetchExercises = async () => {
    console.log('Fetching exercises...');
    try {
      const response = await axios.get(`/users/${userId}/exercises`, {
        headers: {
          Authorization: `Bearer ${authToken}`,
        },
      });
      console.log('Fetched Exercises:', response.data);
      setExercises(response.data);

      // After fetching exercises, fetch logs
      await fetchLogs(response.data); // Pass exercises to fetchLogs

      setIsLoading(false);
    } catch (error) {
      console.error('Failed to fetch exercises:', error);
      setError('Failed to fetch exercises. Please try again later.');
      if (error.response && error.response.status === 401) {
        logout();
        navigate('/login');
      }
      setIsLoading(false);
    }
  };

  // Function to fetch all exercise logs
  const fetchLogs = async (fetchedExercises) => {
    try {
      // Create an array of promises to fetch logs for each exercise
      const logsPromises = fetchedExercises.map((exercise) =>
        axios.get(`/exercises/${exercise.id}/logs`, {
          headers: {
            Authorization: `Bearer ${authToken}`,
          },
        })
      );

      // Wait for all log fetch requests to complete
      const logsResponses = await Promise.all(logsPromises);

      // Group logs by exerciseId and then by date
      const grouped = {};

      logsResponses.forEach((response, index) => {
        const exerciseId = fetchedExercises[index].id;
        const logsData = response.data; // Array of log objects

        grouped[exerciseId] = {};

        logsData.forEach((log) => {
          const date = new Date(log.timestamp).toISOString().split('T')[0]; // Format: YYYY-MM-DD

          if (!grouped[exerciseId][date]) {
            grouped[exerciseId][date] = [];
          }

          grouped[exerciseId][date].push(log);
        });
      });

      setLogsGrouped(grouped);
      console.log('Grouped Logs:', grouped);
    } catch (error) {
      console.error('Failed to fetch logs:', error);
      setError('Failed to fetch logs. Please try again later.');
    }
  };

  // Fetch exercises when component mounts or when logsUpdated changes
  useEffect(() => {
    console.log('useEffect triggered, fetching exercises...');
    fetchExercises();
  }, [userId, logsUpdated]);
  
  const handleExerciseAdded = () => {
    console.log('handleExerciseAdded called');
    setLogsUpdated((prev) => !prev);
  };

  // Function to handle deleting an exercise
  const handleDeleteExercise = async (exerciseId) => {
    const confirmDelete = window.confirm('Are you sure you want to delete this exercise?');
    if (!confirmDelete) return;

    try {
      await axios.delete(`/users/${userId}/exercises/${exerciseId}`);
      setLogsUpdated((prev) => !prev); // Trigger data refresh
    } catch (err) {
      console.error('Error deleting exercise:', err);
      alert('Failed to delete exercise. Please try again.');
    }
  };

  // Function to handle editing an exercise
  const handleEditExercise = (exercise) => {
    setExerciseToEdit(exercise);
    setIsEditing(true);
  };

  // Function to handle submitting the edited exercise
  const handleSubmitEdit = async (updatedExercise) => {
    try {
      await axios.put(`/users/${userId}/exercises/${updatedExercise.id}`, updatedExercise);
      setIsEditing(false);
      setExerciseToEdit(null);
      setLogsUpdated((prev) => !prev); // Trigger data refresh
    } catch (err) {
      console.error('Error updating exercise:', err);
      alert('Failed to update exercise. Please try again.');
    }
  };

  const handleLogAdded = () => {
    console.log('Log added, triggering refresh...');
    setLogsUpdated((prev) => !prev);
  };

  const handleSelectExercise = (exerciseId) => {
    setSelectedExerciseId(exerciseId);
  };

  const handleBack = () => {
    setSelectedExerciseId(null);
  };

  const getGroupedLogsForSelectedExercise = () => {
    if (!selectedExerciseId) return [];
  
    const exerciseLogs = logsGrouped[selectedExerciseId] || {};
    const dates = Object.keys(exerciseLogs);
  
    // Sort dates in descending order
    const sortedDates = dates.sort((a, b) => new Date(b) - new Date(a));
  
    // Map to an array of { date, sets }
    const groupedLogs = sortedDates.map((date) => ({
      date,
      sets: exerciseLogs[date],
    }));
  
    return groupedLogs;
  };
  

  if (isLoading) {
    return <div>Loading...</div>;
  }

  if (error) {
    return <div style={{ color: 'red' }}>{error}</div>;
  }

  const groupedLogsForSelectedExercise = getGroupedLogsForSelectedExercise();

  console.log('HomePage - selectedExerciseId:', selectedExerciseId);
  return (
    <HomeWrapper>

      <h1>Welcome {username}</h1>
      {!selectedExerciseId && (
        <>
          <ExerciseForm
            userId={userId}
            onExerciseAdded={handleExerciseAdded}
          />
          <div>
            <h2>Your Exercises</h2>
            {exercises.length === 0 ? (
              <p>No exercises available. Add your first exercise above!</p>
            ) : (
              <GridContainer>
                {exercises.map((exercise) => {
                  const exerciseLogs = logsGrouped[exercise.id] || {};
                  const dates = Object.keys(exerciseLogs);

                  // Calculate total kg and difference
                  let totalKg = 0;
                  let diffDisplay = null;
                  let variant = 'neutral';

                  if (dates.length > 0) {
                    const sortedDates = dates.sort((a, b) => new Date(b) - new Date(a));
                    const latestDate = sortedDates[0];
                    const latestLogs = exerciseLogs[latestDate];
                    totalKg = latestLogs.reduce(
                      (sum, log) => sum + log.weight * log.reps,
                      0
                    );

                    if (sortedDates.length >= 2) {
                      const secondLatestDate = sortedDates[1];
                      const secondLatestLogs = exerciseLogs[secondLatestDate];
                      const secondTotalKg = secondLatestLogs.reduce(
                        (sum, log) => sum + log.weight * log.reps,
                        0
                      );

                      const diffPercent =
                        secondTotalKg > 0
                          ? ((totalKg - secondTotalKg) / secondTotalKg) * 100
                          : 0;
                      const roundedDiff = Math.round(diffPercent);
                      diffDisplay =
                        roundedDiff >= 0 ? `+${roundedDiff}%` : `${roundedDiff}%`;

                      if (roundedDiff > 0) {
                        variant = 'positive';
                      } else if (roundedDiff < 0) {
                        variant = 'negative';
                      }
                    }
                  }

                  return (
                    <Card key={exercise.id}>
                      <ExerciseHeader>
                        <ExerciseName>{exercise.name}</ExerciseName>
                        <ExerciseDescription>{exercise.description}</ExerciseDescription>
                      </ExerciseHeader>
                      <ExerciseDetails>
                        <TotalKg>Total: {totalKg} kg</TotalKg>
                        {diffDisplay && (
                          <DiffText variant={variant}>Diff: {diffDisplay}</DiffText>
                        )}
                      </ExerciseDetails>
                      <ViewLogsButton onClick={() => handleSelectExercise(exercise.id)}>
                        View Logs
                      </ViewLogsButton>
                      <ButtonGroup>
                        <EditButton onClick={() => handleEditExercise(exercise)}>Edit</EditButton>
                        <DeleteButton onClick={() => handleDeleteExercise(exercise.id)}>Delete</DeleteButton>
                      </ButtonGroup>
                    </Card>
                  );
                })}
              </GridContainer>
            )}
          </div>
        </>
      )}
      
      {selectedExerciseId && (
        <div>
          <BackButton onClick={handleBack}>Back to Exercises</BackButton>
          <ExerciseLogForm
            exerciseId={selectedExerciseId}
            onLogAdded={handleLogAdded}
            lastLog={groupedLogsForSelectedExercise[0]?.sets[0]}
          />
          
          <ExerciseLogs
            exerciseId={selectedExerciseId}
            groupedLogs={groupedLogsForSelectedExercise}
            setLogsUpdated={setLogsUpdated}
          />
        </div>
      )}
      {/* Edit Exercise Modal */}
      {isEditing && exerciseToEdit && (
        <EditExerciseModal>
          <ModalContent>
            <h2>Edit Exercise</h2>
            <EditExerciseForm
              exercise={exerciseToEdit}
              onSubmit={handleSubmitEdit}
              onCancel={() => {
                setIsEditing(false);
                setExerciseToEdit(null);
              }}
            />
          </ModalContent>
        </EditExerciseModal>
      )}
    </HomeWrapper>
  );
};

export default HomePage;