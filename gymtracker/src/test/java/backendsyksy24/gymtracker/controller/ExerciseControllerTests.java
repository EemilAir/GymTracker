package backendsyksy24.gymtracker.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import com.fasterxml.jackson.databind.ObjectMapper;

import backendsyksy24.gymtracker.model.AppUser;
import backendsyksy24.gymtracker.model.Exercise;
import backendsyksy24.gymtracker.model.ExerciseLog;
import backendsyksy24.gymtracker.repository.AppUserRepository;
import backendsyksy24.gymtracker.repository.ExerciseLogRepository;
import backendsyksy24.gymtracker.repository.ExerciseRepository;
import java.util.Optional;
import java.time.LocalDateTime;
import java.util.Arrays;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
public class ExerciseControllerTests {
 
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ExerciseRepository exerciseRepository;

    @MockBean
    private ExerciseLogRepository exerciseLogRepository;

    @MockBean
    private AppUserRepository appUserRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Add a new exercise for a user")
    @WithMockUser(username = "testuser", roles = {"USER"})
    public void testAddExercise() throws Exception {
        // Arrange
        Long userId = 1L;
        AppUser user = new AppUser("testuser", "password", "USER");
        user.setId(userId);

        Exercise exercise = new Exercise("Bench Press", "Chest exercise", user);

        when(appUserRepository.findById(userId)).thenReturn(Optional.of(user));
        when(exerciseRepository.save(ArgumentMatchers.any(Exercise.class))).thenReturn(exercise);

        // Act & Assert
        mockMvc.perform(post("/api/users/{userId}/exercises", userId)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(exercise)))
                .andExpect(status().isOk())
                .andExpect(content().string("Exercise added successfully"));

        // Verify interactions
        verify(appUserRepository, times(1)).findById(userId);
        verify(exerciseRepository, times(1)).save(ArgumentMatchers.any(Exercise.class));
        verifyNoMoreInteractions(appUserRepository, exerciseRepository);
    }

    @Test
    @DisplayName("Get exercises for a user")
    @WithMockUser(username = "testuser", roles = {"USER"})
    public void testGetUserExercises() throws Exception {
        // Arrange
        Long userId = 1L;

        Exercise exercise1 = new Exercise("Bench Press", "Chest exercise", null);
        exercise1.setId(1L);
        Exercise exercise2 = new Exercise("Squat", "Leg exercise", null);
        exercise2.setId(2L);

        when(exerciseRepository.findByUserId(userId)).thenReturn(Arrays.asList(exercise1, exercise2));

        // Act & Assert
        mockMvc.perform(get("/api/users/{userId}/exercises", userId)
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(exercise1.getId()))
                .andExpect(jsonPath("$[0].name").value(exercise1.getName()))
                .andExpect(jsonPath("$[0].description").value(exercise1.getDescription()))
                .andExpect(jsonPath("$[1].id").value(exercise2.getId()))
                .andExpect(jsonPath("$[1].name").value(exercise2.getName()))
                .andExpect(jsonPath("$[1].description").value(exercise2.getDescription()));

        // Verify interactions
        verify(exerciseRepository, times(1)).findByUserId(userId);
        verifyNoMoreInteractions(exerciseRepository);
    }

    @Test
    @DisplayName("Add an exercise log")
    @WithMockUser(username = "testuser", roles = {"USER"})
    public void testAddExerciseLog() throws Exception {
        // Arrange
        Long exerciseId = 1L;
        Exercise exercise = new Exercise("Bench Press", "Chest exercise", null);
        exercise.setId(exerciseId);

        ExerciseLog exerciseLog = new ExerciseLog();
        exerciseLog.setWeight(100);
        exerciseLog.setReps(10);

        when(exerciseRepository.findById(exerciseId)).thenReturn(Optional.of(exercise));
        when(exerciseLogRepository.save(ArgumentMatchers.any(ExerciseLog.class))).thenReturn(exerciseLog);

        // Act & Assert
        mockMvc.perform(post("/api/exercises/{exerciseId}/logs", exerciseId)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(exerciseLog)))
                .andExpect(status().isOk())
                .andExpect(content().string("Exercise log added successfully"));

        // Verify interactions
        verify(exerciseRepository, times(1)).findById(exerciseId);
        verify(exerciseLogRepository, times(1)).save(ArgumentMatchers.any(ExerciseLog.class));
        verifyNoMoreInteractions(exerciseRepository, exerciseLogRepository);
    } 

    @Test
    @DisplayName("Get logs for an exercise")
    @WithMockUser(username = "testuser", roles = {"USER"})
    public void testGetExerciseLogs() throws Exception {
        // Arrange
        Long exerciseId = 1L;

        ExerciseLog log1 = new ExerciseLog();
        log1.setId(1L);
        log1.setWeight(100);
        log1.setReps(10);
        log1.setTimestamp(LocalDateTime.now());

        ExerciseLog log2 = new ExerciseLog();
        log2.setId(2L);
        log2.setWeight(105);
        log2.setReps(8);
        log2.setTimestamp(LocalDateTime.now());

        when(exerciseLogRepository.findByExerciseId(exerciseId)).thenReturn(Arrays.asList(log1, log2));

        // Act & Assert
        mockMvc.perform(get("/api/exercises/{exerciseId}/logs", exerciseId)
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(log1.getId()))
                .andExpect(jsonPath("$[0].weight").value(log1.getWeight()))
                .andExpect(jsonPath("$[0].reps").value(log1.getReps()))
                .andExpect(jsonPath("$[1].id").value(log2.getId()))
                .andExpect(jsonPath("$[1].weight").value(log2.getWeight()))
                .andExpect(jsonPath("$[1].reps").value(log2.getReps()));

        // Verify interactions
        verify(exerciseLogRepository, times(1)).findByExerciseId(exerciseId);
        verifyNoMoreInteractions(exerciseLogRepository);
    } 

    @Test
    @DisplayName("Update an exercise")
    @WithMockUser(username = "testuser", roles = {"USER"})
    public void testUpdateExercise() throws Exception {
        // Arrange
        Long userId = 1L;
        Long exerciseId = 1L;
        Exercise existingExercise = new Exercise("Bench Press", "Chest exercise", null);
        existingExercise.setId(exerciseId);

        Exercise updatedExercise = new Exercise("Bench Press Updated", "Updated description", null);

        when(exerciseRepository.findById(exerciseId)).thenReturn(Optional.of(existingExercise));
        when(exerciseRepository.save(ArgumentMatchers.any(Exercise.class))).thenReturn(existingExercise);

        // Act & Assert
        mockMvc.perform(put("/api/users/{userId}/exercises/{exerciseId}", userId, exerciseId)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedExercise)))
                .andExpect(status().isOk())
                .andExpect(content().string("Exercise updated successfully"));

        // Verify interactions
        verify(exerciseRepository, times(1)).findById(exerciseId);
        verify(exerciseRepository, times(1)).save(ArgumentMatchers.any(Exercise.class));
        verifyNoMoreInteractions(exerciseRepository);
    }

    @Test
    @DisplayName("Delete an exercise")
    @WithMockUser(username = "testuser", roles = {"USER"})
    public void testDeleteExercise() throws Exception {
        // Arrange
        Long userId = 1L;
        Long exerciseId = 1L;
        Exercise exercise = new Exercise("Bench Press", "Chest exercise", null);
        exercise.setId(exerciseId);

        when(exerciseRepository.findById(exerciseId)).thenReturn(Optional.of(exercise));
        doNothing().when(exerciseRepository).delete(exercise);

        // Act & Assert
        mockMvc.perform(delete("/api/users/{userId}/exercises/{exerciseId}", userId, exerciseId)
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("Exercise deleted successfully"));

        // Verify interactions
        verify(exerciseRepository, times(1)).findById(exerciseId);
        verify(exerciseRepository, times(1)).delete(exercise);
        verifyNoMoreInteractions(exerciseRepository);
    }

}
