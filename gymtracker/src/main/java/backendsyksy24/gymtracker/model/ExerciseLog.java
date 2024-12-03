package backendsyksy24.gymtracker.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

// Entity class for the exercise log table
@Entity
@Table(name="ExerciseLogTable")
public class ExerciseLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime timestamp;
    @Positive
    @Column(nullable = false)
    private int weight;
    @Positive
    @Column(nullable = false)
    private int reps;

    // Each log belongs to an exercise
    @ManyToOne
    @JsonBackReference
    private Exercise exercise;

    // Constructors
    public ExerciseLog() {
    }

    public ExerciseLog(Exercise exercise, LocalDateTime timestamp, int weight, int reps) {
        this.exercise = exercise;
        this.timestamp = timestamp;
        this.weight = weight;
        this.reps = reps;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public int getWeight() {
        return weight;
    }
 
    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getReps() {
        return reps;
    }
 
    public void setReps(int reps) {
        this.reps = reps;
    }

    public Exercise getExercise() {
        return exercise;
    }
 
    public void setExercise(Exercise exercise) {
        this.exercise = exercise;
    }
}