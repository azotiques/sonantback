package org.azotiques.sonantv1.guild.repositories;

import jakarta.persistence.Column;
import org.azotiques.sonantv1.guild.entities.Guild;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface GuildRepository extends JpaRepository<Guild, Long> {

    @Query(
            """
    SELECT g FROM Guild g
    WHERE :userId MEMBER OF g.memberIds
"""
    )
    List<Guild> findByMember(Long userId);

    @Query(
            """
    SELECT g FROM Guild g
    WHERE g.id = :id
    AND :userId MEMBER OF g.memberIds
"""
    )
    Optional<Guild> findByIdAndMember(Long id, Long userId);
    @Query(
            """
    SELECT g FROM Guild g
    JOIN g.channels c
    WHERE g.id = :id
    AND c.id = :channelId
    AND :userId MEMBER OF g.memberIds
"""
    )
    Optional<Guild> findByMemberAndIdAndChannelId(Long userId, Long id, Long channelId);

}
