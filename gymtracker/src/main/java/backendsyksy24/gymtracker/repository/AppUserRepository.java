package backendsyksy24.gymtracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import backendsyksy24.gymtracker.model.AppUser;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    AppUser findByUsername(String username);
    boolean existsByUsername(String username);
}