package org.azotiques.sonantv1.messaging.services;

import lombok.RequiredArgsConstructor;
import org.azotiques.sonantv1.guild.GuildApi;
import org.azotiques.sonantv1.messaging.dtos.MessageDto;
import org.azotiques.sonantv1.messaging.dtos.MessageRequest;
import org.azotiques.sonantv1.messaging.entities.Message;
import org.azotiques.sonantv1.messaging.exceptions.MessageNotFoundException;
import org.azotiques.sonantv1.messaging.mappers.MessageMapper;
import org.azotiques.sonantv1.messaging.repositories.MessageRepository;
import org.azotiques.sonantv1.user.UserApi;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final GuildApi guildApi;
    private final UserApi userApi;

    public void deleteMessage(Jwt jwt, MessageRequest messageRequest, Long guildId, Long channelId) {
        Long userId = userApi.requireId(jwt.getSubject());

        Long guild = guildApi.findGuildByMemberAndIdAndChannelId(userId, guildId, channelId);
        Long channel = guildApi.findChannelByMemberAndIdAndGuildId(userId, channelId, guildId);

        Message message = messageRepository.findByIdAndGuildAndChannel(messageRequest.messageId(), guild, channel)
                .orElseThrow(() -> new MessageNotFoundException("Message not found"));

        messageRepository.delete(message);
    }

    public List<MessageDto> getMessages(Jwt jwt, Long guildId, Long channelId) {
        Long userId = userApi.requireId(jwt.getSubject());
        guildApi.findGuildByMemberAndIdAndChannelId(userId, guildId, channelId);
        guildApi.findChannelByMemberAndIdAndGuildId(userId, channelId, guildId);

        List<Message> messages = messageRepository.findAllByChannelAndGuild(channelId, guildId);

        Set<Long> authorIds = messages.stream()
                .map(Message::getUsers)
                .collect(Collectors.toSet());

        return MessageMapper.toMessageDtos(messages, userApi.findAllById(authorIds));
    }

}
