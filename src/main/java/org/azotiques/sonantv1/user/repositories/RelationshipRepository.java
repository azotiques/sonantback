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
            JOIN FETCH r.incomingUser
            JOIN FETCH r.outgoingUser
            WHERE r.incomingUser = :user
            """
    )
    List<Relationship> findByIncomingUser(User user);

    @Query(
            """
            SELECT r FROM Relationship r
            JOIN FETCH r.incomingUser
            JOIN FETCH r.outgoingUser
            WHERE (r.outgoingUser = :user OR r.incomingUser = :user)
              AND r.status = :status
            """
    )
    List<Relationship> findByUserAndStatus(User user, Status status);

    List<Relationship> findByOutgoingUserAndStatus(User user, Status status);

    Relationship findByOutgoingUser(User outgoingUser);

    Optional<Relationship> findByIncomingUserAndOutgoingUser(User incomingUser, User outgoingUser);

    List<Relationship> findByOutgoingUserOrIncomingUserAndStatus(User incomingUser, User outgoingUser, Status status);

    List<Relationship> findByOutgoingUserOrIncomingUser(User user, User user1);
}
