package backendsyksy24.gymtracker.controller;

import backendsyksy24.gymtracker.model.AppUser;
import backendsyksy24.gymtracker.dto.RegisterRequest;
import backendsyksy24.gymtracker.dto.UserDTO;
import backendsyksy24.gymtracker.repository.AppUserRepository;
import backendsyksy24.gymtracker.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    /* Registers a new user with the provided registration details. */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest registerRequest) {
        String lowerUsername = registerRequest.getUsername().toLowerCase();
        // Check if username already exists
        if (appUserRepository.existsByUsername(lowerUsername)) {
            return ResponseEntity.badRequest().body("Username is already taken");
        }
        // Check if password is strong enough
        if (!isStrongPassword(registerRequest.getPasswordHash())) {
            return ResponseEntity.badRequest().body("Password is not strong enough");
        }
        // Encode the password and set the role
        String encodedPassword = bCryptPasswordEncoder.encode(registerRequest.getPasswordHash());

        AppUser user = new AppUser();
        user.setUsername(lowerUsername);
        user.setPasswordHash(encodedPassword);
        user.setRole("ROLE_USER"); // Automatically set role to USER
        appUserRepository.save(user);
        return ResponseEntity.ok("User registered successfully");
    }

    /* Logs in the user with the provided login details. */
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody AppUser user) {
        AppUser existingUser = appUserRepository.findByUsername(user.getUsername().toLowerCase());
        if (existingUser != null && bCryptPasswordEncoder.matches(user.getPasswordHash(), existingUser.getPasswordHash())) {
            String token = jwtUtil.generateToken(existingUser.getUsername());
            String role = existingUser.getRole();

            // Populate UserDTO with token and role
            UserDTO userDTO = new UserDTO(existingUser.getId(), existingUser.getUsername(), role, token);
            return ResponseEntity.ok(userDTO);
        }
        return ResponseEntity.status(401).body("Invalid credentials");
    }

    /* Returns a list of all registered users.*/
    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDTO>> getAllUsernames() {
        List<UserDTO> users = appUserRepository.findAll().stream()
                .map(user -> new UserDTO(user.getId(), user.getUsername(), user.getRole()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(users);
    }

    /* Deletes a specific user by ID. */
    @DeleteMapping("/users/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        Optional<AppUser> userOptional = appUserRepository.findById(id);
        if (userOptional.isPresent()) {
            appUserRepository.deleteById(id);
            return ResponseEntity.ok("User deleted successfully");
        } else {
            return ResponseEntity.status(404).body("User not found");
        }
    }

    /* Criteria:
    * - At least 8 characters
    * - Contains at least one uppercase letter
    * - Contains at least one lowercase letter
    * - Contains at least one digit
    * - Contains at least one special character 
    */
    private boolean isStrongPassword(String password) {
        if (password == null) return false;
        // Regex pattern to enforce password strength
        String pattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&#^()\\-_=+{};:,<.>]).{8,}$";
        return password.matches(pattern);
    }
}