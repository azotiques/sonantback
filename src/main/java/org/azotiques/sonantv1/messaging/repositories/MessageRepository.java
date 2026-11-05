package org.azotiques.sonantv1.messaging.repositories;

import org.azotiques.sonantv1.messaging.entities.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query(
            """
    SELECT m FROM Message m
    WHERE :channelId = m.channel
"""
    )
    List<Message> findAllByChannel(Long channelId);


    @Query(
            """
    SELECT m FROM Message m
    WHERE :channelId = m.channel
    AND :guildId = m.guild
"""
    )
    List<Message> findAllByChannelAndGuild(Long channelId, Long guildId);


    @Query("""
    SELECT m FROM Message m
    WHERE :id = m.id
    AND :channelId = m.channel
    AND :guildId = m.guild
""")
    Optional<Message> findByIdAndGuildAndChannel(Long id, Long guildId, Long channelId);
}
