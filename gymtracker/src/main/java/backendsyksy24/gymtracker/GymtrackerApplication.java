package backendsyksy24.gymtracker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import backendsyksy24.gymtracker.model.AppUser;
import backendsyksy24.gymtracker.model.Exercise;
import backendsyksy24.gymtracker.model.ExerciseLog;
import backendsyksy24.gymtracker.repository.AppUserRepository;
import backendsyksy24.gymtracker.repository.ExerciseRepository;
import backendsyksy24.gymtracker.repository.ExerciseLogRepository;


import java.time.LocalDateTime;

@SpringBootApplication
public class GymtrackerApplication {
    private static final Logger log = LoggerFactory.getLogger(GymtrackerApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(GymtrackerApplication.class, args);
    }

    // Create some test data, but only when not running tests
    @Bean
    @Profile("!test")
    public CommandLineRunner demo(AppUserRepository userRepository,
                              ExerciseRepository exerciseRepository,
                              ExerciseLogRepository exerciseLogRepository) {
    return (args) -> {

            // Create users: admin/admin123 user/user123
            if (userRepository.findByUsername("user") == null && userRepository.findByUsername("admin") == null) {
                AppUser user1 = new AppUser("user", "$2y$12$Tzl1BXZ0AmE5BzWbA7V8Fu95XZWYMRWSNDPadDf7gbGCmSFouldta", "USER");
                AppUser user2 = new AppUser("admin", "$2y$12$LANDGea1.7hNh3Pk8BMpDuquVpqGbxHOmNsiIBIiiQg5s0fD83KaC", "ADMIN");
                userRepository.save(user1);
                userRepository.save(user2);
                log.info("Saved a couple of users");

                // Create exercises for admin
                AppUser admin = userRepository.findByUsername("admin");
                Exercise exercise1 = new Exercise("Bench Press", "6x3", admin);
                Exercise exercise2 = new Exercise("Squats", "6x3", admin);
                exerciseRepository.save(exercise1);
                exerciseRepository.save(exercise2);
                log.info("Saved exercises for admin");

                // Create exercise logs for exercise1
                // Corrected parameter order: (Exercise exercise, LocalDateTime timestamp, int weight, int repetitions)
                ExerciseLog log1 = new ExerciseLog(exercise1, LocalDateTime.now().minusDays(3), 100, 6);
                ExerciseLog log2 = new ExerciseLog(exercise1, LocalDateTime.now().minusDays(2), 105, 6);
                ExerciseLog log3 = new ExerciseLog(exercise1, LocalDateTime.now().minusDays(1), 110, 6);
                exerciseLogRepository.save(log1);
                exerciseLogRepository.save(log2);
                exerciseLogRepository.save(log3);
                log.info("Saved exercise logs for Bench Press");

                // Create exercise logs for exercise2
                ExerciseLog log4 = new ExerciseLog(exercise2, LocalDateTime.now().minusDays(3), 150, 5);
                ExerciseLog log5 = new ExerciseLog(exercise2, LocalDateTime.now().minusDays(2), 155, 5);
                ExerciseLog log6 = new ExerciseLog(exercise2, LocalDateTime.now().minusDays(1), 160, 5);
                exerciseLogRepository.save(log4);
                exerciseLogRepository.save(log5);
                exerciseLogRepository.save(log6);
                log.info("Saved exercise logs for Squats");
            }
        };

    }
}