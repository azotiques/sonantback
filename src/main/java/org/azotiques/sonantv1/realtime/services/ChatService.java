package org.azotiques.sonantv1.realtime.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.azotiques.sonantv1.guild.GuildApi;
import org.azotiques.sonantv1.messaging.MessagingApi;
import org.azotiques.sonantv1.messaging.SendMessageRequest;
import org.azotiques.sonantv1.messaging.SendMessageResponse;
import org.azotiques.sonantv1.realtime.dtos.ChatMessage;
import org.azotiques.sonantv1.user.UserApi;
import org.azotiques.sonantv1.user.UserDto;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final UserApi userApi;
    private final GuildApi guildApi;
    private final MessagingApi messagingApi;

    @Transactional
    public SendMessageResponse save(Jwt jwt, ChatMessage chatMessage, Long guildId, Long channelId) {
        UserDto user = userApi.findDtoBySub(jwt.getSubject());
        Long guild = guildApi.findGuildByMemberAndIdAndChannelId(user.id(), guildId, channelId);
        Long channel = guildApi.findChannelByMemberAndIdAndGuildId(user.id(), channelId, guildId);

        SendMessageRequest messageDto = new SendMessageRequest(
                chatMessage.message(),
                user,
                guild,
               channel
        );

        return messagingApi.sendMessage(messageDto);
    }

}
