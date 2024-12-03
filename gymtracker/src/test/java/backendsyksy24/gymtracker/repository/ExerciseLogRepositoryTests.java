package backendsyksy24.gymtracker.repository;

import backendsyksy24.gymtracker.model.AppUser;
import backendsyksy24.gymtracker.model.Exercise;
import backendsyksy24.gymtracker.model.ExerciseLog;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@DataJpaTest
public class ExerciseLogRepositoryTests {

    @Autowired
    private ExerciseLogRepository exerciseLogRepository;

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Autowired
    private AppUserRepository appUserRepository;

    @Test
    @DisplayName("Save and retrieve ExerciseLog")
    public void testSaveAndRetrieveExerciseLog() {
        // Arrange
        AppUser user = new AppUser("testuser", "password", "USER");
        appUserRepository.save(user);

        Exercise exercise = new Exercise("Bench Press", "Chest exercise", user);
        exerciseRepository.save(exercise);

        ExerciseLog exerciseLog = new ExerciseLog(exercise, LocalDateTime.now(), 100, 10);

        // Act
        exerciseLogRepository.save(exerciseLog);

        // Assert
        List<ExerciseLog> logs = exerciseLogRepository.findAll();
        assertEquals(1, logs.size());

        ExerciseLog retrievedLog = logs.get(0);
        assertEquals(100, retrievedLog.getWeight());
        assertEquals(10, retrievedLog.getReps());
        assertEquals(exercise.getId(), retrievedLog.getExercise().getId());
    }

    @Test
    @DisplayName("Find ExerciseLog by ID")
    public void testFindExerciseLogById() {
        // Arrange
        AppUser user = new AppUser("testuser", "password", "USER");
        appUserRepository.save(user);

        Exercise exercise = new Exercise("Squat", "Leg exercise", user);
        exerciseRepository.save(exercise);

        ExerciseLog exerciseLog = new ExerciseLog(exercise, LocalDateTime.now(), 150, 5);
        exerciseLogRepository.save(exerciseLog);

        Long logId = exerciseLog.getId();

        // Act
        Optional<ExerciseLog> optionalLog = exerciseLogRepository.findById(logId);

        // Assert
        assertTrue(optionalLog.isPresent());
        ExerciseLog retrievedLog = optionalLog.get();
        assertEquals(150, retrievedLog.getWeight());
        assertEquals(5, retrievedLog.getReps());
        assertEquals(exercise.getId(), retrievedLog.getExercise().getId());
    }

    @Test
    @DisplayName("Delete ExerciseLog")
    public void testDeleteExerciseLog() {
        // Arrange
        AppUser user = new AppUser("testuser", "password", "USER");
        appUserRepository.save(user);

        Exercise exercise = new Exercise("Deadlift", "Back exercise", user);
        exerciseRepository.save(exercise);

        ExerciseLog exerciseLog = new ExerciseLog(exercise, LocalDateTime.now(), 200, 3);
        exerciseLogRepository.save(exerciseLog);

        Long logId = exerciseLog.getId();

        // Act
        exerciseLogRepository.deleteById(logId);

        // Assert
        Optional<ExerciseLog> optionalLog = exerciseLogRepository.findById(logId);
        assertFalse(optionalLog.isPresent());
    }
}