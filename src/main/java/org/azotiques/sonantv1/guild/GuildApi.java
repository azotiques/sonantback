package org.azotiques.sonantv1.guild;

import org.springframework.security.oauth2.jwt.Jwt;

public interface GuildApi {
    void guildCheck(Jwt jwt, String guildId, String channelId);
    Long findGuildByMemberAndIdAndChannelId(Long userId, Long id, Long channelId);
    Long findChannelByMemberAndIdAndGuildId(Long userId, Long id, Long guildId);
}
