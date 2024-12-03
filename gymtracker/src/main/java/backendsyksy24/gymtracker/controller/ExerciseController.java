package backendsyksy24.gymtracker.controller;

import backendsyksy24.gymtracker.dto.ExerciseDTO;
import backendsyksy24.gymtracker.model.Exercise;
import backendsyksy24.gymtracker.model.ExerciseLog;
import backendsyksy24.gymtracker.repository.ExerciseRepository;
import backendsyksy24.gymtracker.repository.ExerciseLogRepository;
import backendsyksy24.gymtracker.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/* Controller for managing exercises and their logs. */
@RestController
@RequestMapping("/api")
public class ExerciseController {

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Autowired
    private ExerciseLogRepository exerciseLogRepository;

    @Autowired
    private AppUserRepository appUserRepository;


    /* Adds a new exercise for the specified user. */
    @PostMapping("/users/{userId}/exercises")
    public ResponseEntity<?> addExercise(@PathVariable Long userId, @RequestBody Exercise exercise) {
        return appUserRepository.findById(userId).map(user -> {
            exercise.setUser(user);
            exerciseRepository.save(exercise);
            return ResponseEntity.ok("Exercise added successfully");
        }).orElse(ResponseEntity.status(404).body("User not found"));
    }

    /* Returns all exercises for the specified user. */
    @GetMapping("/users/{userId}/exercises")
    public ResponseEntity<List<ExerciseDTO>> getUserExercises(@PathVariable Long userId) {
        List<Exercise> exercises = exerciseRepository.findByUserId(userId);
        List<ExerciseDTO> dtoList = exercises.stream()
            .map(exercise -> new ExerciseDTO(exercise.getId(), exercise.getName(), exercise.getDescription()))
            .collect(Collectors.toList());
        return ResponseEntity.ok(dtoList);
    }

    /* Adds a new log to the specified exercise. */
    @PostMapping("/exercises/{exerciseId}/logs")
    public ResponseEntity<?> addExerciseLog(@PathVariable Long exerciseId, @RequestBody ExerciseLog exerciseLog) {
        return exerciseRepository.findById(exerciseId).map(exercise -> {
            exerciseLog.setExercise(exercise);
            exerciseLog.setTimestamp(LocalDateTime.now()); // Set the timestamp
            exerciseLogRepository.save(exerciseLog);
            return ResponseEntity.ok("Exercise log added successfully");
        }).orElse(ResponseEntity.status(404).body("Exercise not found"));
    }

    /* Returns all logs for the specified exercise. */
    @GetMapping("/exercises/{exerciseId}/logs")
    public ResponseEntity<List<ExerciseLog>> getExerciseLogs(@PathVariable Long exerciseId) {
        List<ExerciseLog> exerciseLogs = exerciseLogRepository.findByExerciseId(exerciseId);
        return ResponseEntity.ok(exerciseLogs);
    }

    /* Updates an existing exercise for the specified user. */
    @PutMapping("/users/{userId}/exercises/{exerciseId}")
    public ResponseEntity<?> updateExercise(@PathVariable Long userId, @PathVariable Long exerciseId, @RequestBody Exercise exerciseDetails) {
        return exerciseRepository.findById(exerciseId).map(exercise -> {
            exercise.setName(exerciseDetails.getName());
            exercise.setDescription(exerciseDetails.getDescription());
            exerciseRepository.save(exercise);
            return ResponseEntity.ok("Exercise updated successfully");
        }).orElse(ResponseEntity.status(404).body("Exercise not found"));
    }

    /* Deletes an existing exercise for the specified user. */
    @DeleteMapping("/users/{userId}/exercises/{exerciseId}")
    public ResponseEntity<?> deleteExercise(@PathVariable Long userId, @PathVariable Long exerciseId) {
        return exerciseRepository.findById(exerciseId).map(exercise -> {
            exerciseRepository.delete(exercise);
            return ResponseEntity.ok("Exercise deleted successfully");
        }).orElse(ResponseEntity.status(404).body("Exercise not found"));
    }

    /* Deletes an existing exercise log. */
    @DeleteMapping("/exercises/{exerciseId}/logs/{logId}")
    public ResponseEntity<?> deleteExerciseLog(@PathVariable Long exerciseId, @PathVariable Long logId) {
        return exerciseLogRepository.findById(logId).map(exerciseLog -> {
            if (!exerciseLog.getExercise().getId().equals(exerciseId)) {
                return ResponseEntity.status(400).body("ExerciseLog does not belong to the specified Exercise");
            }
            exerciseLogRepository.delete(exerciseLog);
            return ResponseEntity.ok("Exercise log deleted successfully");
        }).orElse(ResponseEntity.status(404).body("ExerciseLog not found"));
    }
}