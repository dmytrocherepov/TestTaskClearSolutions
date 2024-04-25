package org.example.testtaskclearsolutions.repository;

import org.example.testtaskclearsolutions.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @EntityGraph(attributePaths = "roles")
    Optional<User> findByEmail(String email);

    Boolean existsByEmail(String email);

    @EntityGraph(attributePaths = "roles")
    List<User> findUsersByBirthDateBetween(LocalDate from, LocalDate to);


}
