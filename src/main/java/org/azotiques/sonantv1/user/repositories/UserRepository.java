package org.azotiques.sonantv1.user.repositories;

import org.azotiques.sonantv1.user.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findBySub(String sub);

    Optional<User> findByUsername(String username);
}
