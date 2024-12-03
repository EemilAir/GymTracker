package backendsyksy24.gymtracker.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;

// Entity class for the user table
@Entity
@Table(name="UserTable")
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // The username must be unique
    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;
    
    @Column(nullable = false)
    private String role;

    // Each user can have multiple exercises
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonManagedReference // Manages serialization of exercises
    private List<Exercise> exercises;

    public AppUser() {
    }

    public AppUser(String username, String password, String role) {
        this.username = username.toLowerCase();
        this.password = password;
        this.role = "ROLE_" + role.toUpperCase(); // Automatically set role to ROLE_ prefix
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return password;
    }

    public void setPasswordHash(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }
    // Set role with ROLE_ prefix
    public void setRole(String role) {
        this.role = "ROLE_" + role.toUpperCase();
    }
}