package backendsyksy24.gymtracker.repository;

import backendsyksy24.gymtracker.model.ExerciseLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExerciseLogRepository extends JpaRepository<ExerciseLog, Long> {
    List<ExerciseLog> findByExerciseId(Long exerciseId);
}