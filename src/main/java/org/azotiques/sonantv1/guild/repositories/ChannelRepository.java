package org.azotiques.sonantv1.guild.repositories;

import org.azotiques.sonantv1.guild.entities.Channel;
import org.azotiques.sonantv1.guild.entities.Guild;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ChannelRepository extends JpaRepository<Channel, Long> {

    long countByGuild(Guild guild);

    @Query("""
       SELECT c FROM Channel c
       WHERE :userId MEMBER OF c.userIds
       AND :channelId = c.id
""")
    Optional<Channel> findByUserAndId(Long userId, Long channelId);

    @Query("""
    SELECT c FROM Channel c
    JOIN c.guild g
    WHERE :userId MEMBER OF c.userIds
    AND :channelId = c.id
    AND g = :guild
""")
    Optional<Channel> findByUserAndIdAndGuild(Long userId, Long channelId, Guild guild);
}
