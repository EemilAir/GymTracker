package backendsyksy24.gymtracker.repository;

import backendsyksy24.gymtracker.model.AppUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@DataJpaTest
public class AppUserRepositoryTests {

    @Autowired
    private AppUserRepository appUserRepository;

    @Test
    @DisplayName("Save a valid AppUser and retrieve it by username")
    public void testSaveAndFindByUsername() {
        // Arrange
        AppUser user = new AppUser();
        user.setUsername("testuser");
        user.setPasswordHash("StrongP@ssw0rd!");
        user.setRole("USER");

        // Act
        appUserRepository.save(user);
        AppUser foundUser = appUserRepository.findByUsername("testuser");

        // Assert
        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getUsername()).isEqualTo("testuser");
        assertThat(foundUser.getRole()).isEqualTo("ROLE_USER");
    }

    @Test
    @DisplayName("Check existence of a user by username")
    public void testExistsByUsername() {
        // Arrange
        AppUser user = new AppUser();
        user.setUsername("anotheruser");
        user.setPasswordHash("AnotherStr0ngP@ss!");
        user.setRole("USER");
        appUserRepository.save(user);

        // Act
        boolean exists = appUserRepository.existsByUsername("anotheruser");
        boolean notExists = appUserRepository.existsByUsername("nonexistent");

        // Assert
        assertThat(exists).isTrue();
        assertThat(notExists).isFalse();
    }

    @Test
    @DisplayName("Saving a user with duplicate username should fail")
    public void testDuplicateUsername() {
        // Arrange
        AppUser user1 = new AppUser();
        user1.setUsername("duplicateUser");
        user1.setPasswordHash("DuplicaT3P@ss!");
        user1.setRole("USER");
        appUserRepository.save(user1);

        AppUser user2 = new AppUser();
        user2.setUsername("duplicateUser"); // Same username
        user2.setPasswordHash("AnotherP@ssw0rd!");
        user2.setRole("USER");

        // Act & Assert
        org.junit.jupiter.api.Assertions.assertThrows(
            org.springframework.dao.DataIntegrityViolationException.class,
            () -> appUserRepository.saveAndFlush(user2),
            "Expected DataIntegrityViolationException due to duplicate username"
        );
    }

    @Test
    @DisplayName("Find user by ID")
    public void testFindById() {
        // Arrange
        AppUser user = new AppUser();
        user.setUsername("findByIdUser");
        user.setPasswordHash("FindBy1D@Pass!");
        user.setRole("USER");
        AppUser savedUser = appUserRepository.save(user);

        // Act
        AppUser foundUser = appUserRepository.findById(savedUser.getId()).orElse(null);

        // Assert
        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getUsername()).isEqualTo("findByIdUser");
    }

    @Test
    @DisplayName("Delete a user by ID")
    public void testDeleteUserById() {
        // Arrange
        AppUser user = new AppUser();
        user.setUsername("deleteUser");
        user.setPasswordHash("DeleteP@ssw0rd!");
        user.setRole("USER");
        AppUser savedUser = appUserRepository.save(user);

        // Act
        appUserRepository.deleteById(savedUser.getId());
        boolean exists = appUserRepository.existsById(savedUser.getId());

        // Assert
        assertThat(exists).isFalse();
    }
}