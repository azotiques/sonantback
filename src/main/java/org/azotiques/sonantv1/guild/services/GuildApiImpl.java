package org.azotiques.sonantv1.guild.services;

import lombok.RequiredArgsConstructor;
import org.azotiques.sonantv1.guild.GuildApi;
import org.azotiques.sonantv1.guild.entities.Guild;
import org.azotiques.sonantv1.guild.exceptions.ChannelNotFoundException;
import org.azotiques.sonantv1.guild.exceptions.GuildNotFoundException;
import org.azotiques.sonantv1.guild.repositories.ChannelRepository;
import org.azotiques.sonantv1.guild.repositories.GuildRepository;
import org.azotiques.sonantv1.user.UserApi;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GuildApiImpl implements GuildApi {

    private final UserApi userApi;
    private final GuildRepository guildRepository;
    private final ChannelRepository channelRepository;
    private final GuildService guildService;

    @Override
    public void guildCheck(Jwt jwt, String guildId, String channelId) {
        Long userId = userApi.requireId(jwt.getSubject());

        if (guildRepository.findByMemberAndIdAndChannelId(userId, Long.parseLong(guildId), Long.parseLong(channelId)).isEmpty()) {
            throw new AccessDeniedException("Access denied");
        }
    }

    @Override
    public Long findGuildByMemberAndIdAndChannelId(Long userId, Long id, Long channelId) {
        return guildRepository.findByMemberAndIdAndChannelId(userId, id, channelId)
                .orElseThrow(ChannelNotFoundException::new)
                .getId();
    }

    @Override
    public Long findChannelByMemberAndIdAndGuildId(Long userId, Long id, Long guildId) {
        Guild guild = guildRepository.findById(guildId).orElseThrow(GuildNotFoundException::new);
        return channelRepository.findByUserAndIdAndGuild(userId, id, guild)
                .orElseThrow(ChannelNotFoundException::new)
                .getId();
    }

}
