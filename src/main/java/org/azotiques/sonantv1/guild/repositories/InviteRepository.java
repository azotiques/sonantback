package org.azotiques.sonantv1.guild.repositories;

import org.azotiques.sonantv1.guild.entities.Invite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InviteRepository extends JpaRepository<Invite, Long> {
    Optional<Invite> findByInviteCode(String inviteCode);
}
