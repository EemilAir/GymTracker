package backendsyksy24.gymtracker.repository;

import backendsyksy24.gymtracker.model.AppUser;
import backendsyksy24.gymtracker.model.Exercise;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@DataJpaTest
public class ExerciseRepositoryTests {

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Autowired
    private AppUserRepository appUserRepository;

    @Test
    @DisplayName("Save and find Exercise by ID")
    public void testSaveAndFindExerciseById() {
        // Arrange
        AppUser user = new AppUser();
        user.setUsername("exerciseUser");
        user.setPasswordHash("Exerc1seP@ss!");
        user.setRole("ROLE_USER");
        appUserRepository.save(user);

        Exercise exercise = new Exercise();
        exercise.setName("Push Up");
        exercise.setDescription("Upper body exercise");
        exercise.setUser(user);

        // Act
        Exercise savedExercise = exerciseRepository.save(exercise);
        Optional<Exercise> foundExercise = exerciseRepository.findById(savedExercise.getId());

        // Assert
        assertThat(foundExercise).isPresent();
        assertThat(foundExercise.get().getName()).isEqualTo("Push Up");
        assertThat(foundExercise.get().getUser()).isEqualTo(user);
    }

    @Test
    @DisplayName("Delete Exercise by ID")
    public void testDeleteExerciseById() {
        // Arrange
        AppUser user = new AppUser();
        user.setUsername("deleteExerciseUser");
        user.setPasswordHash("DelExerc1seP@ss!");
        user.setRole("ROLE_USER");
        appUserRepository.save(user);

        Exercise exercise = new Exercise();
        exercise.setName("Squats");
        exercise.setDescription("Lower body exercise");
        exercise.setUser(user);
        Exercise savedExercise = exerciseRepository.save(exercise);

        // Act
        exerciseRepository.deleteById(savedExercise.getId());
        Optional<Exercise> foundExercise = exerciseRepository.findById(savedExercise.getId());

        // Assert
        assertThat(foundExercise).isNotPresent();
    }
}