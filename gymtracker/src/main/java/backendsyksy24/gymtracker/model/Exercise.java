package backendsyksy24.gymtracker.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.util.List;

// Entity class for the exercise table
@Entity
@Table(name="ExerciseTable")
public class Exercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;
    private String description;

    // Each exercise belongs to a user
    @ManyToOne
    @JsonBackReference // Prevents serialization of the user in Exercise
    private AppUser user;

    // Each exercise can have multiple logs
    @OneToMany(mappedBy = "exercise", cascade = CascadeType.ALL)
    @JsonManagedReference // Manages serialization of exerciseLogs
    private List<ExerciseLog> exerciseLogs;

    public Exercise() {
    }

    public Exercise(String name, String description, AppUser user) {
        this.name = name;
        this.description = description;
        this.user = user;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public AppUser getUser() {
        return user;
    }

    public void setUser(AppUser user) {
        this.user = user;
    }

    public List<ExerciseLog> getExerciseLogs() {
        return exerciseLogs;
    }

    public void setExerciseLogs(List<ExerciseLog> exerciseLogs) {
        this.exerciseLogs = exerciseLogs;
    }
}