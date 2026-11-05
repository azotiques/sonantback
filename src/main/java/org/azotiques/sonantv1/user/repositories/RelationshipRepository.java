package org.azotiques.sonantv1.user.repositories;

import org.azotiques.sonantv1.user.dtos.Status;
import org.azotiques.sonantv1.user.entities.Relationship;
import org.azotiques.sonantv1.user.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RelationshipRepository extends JpaRepository<Relationship, Long> {
    @Query(
            """
            SELECT r FROM Relationship r
            WHERE r.incomingUser = :user
            """
    )
    List<Relationship> findByIncomingUser(User user);

    List<Relationship> findByOutgoingUserAndStatus(User user, Status status);

    Relationship findByOutgoingUser(User outgoingUser);

    Optional<Relationship> findByIncomingUserAndOutgoingUser(User incomingUser, User outgoingUser);

    List<Relationship> findByOutgoingUserOrIncomingUserAndStatus(User incomingUser, User outgoingUser, Status status);
}
