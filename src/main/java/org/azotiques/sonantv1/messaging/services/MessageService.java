package org.azotiques.sonantv1.messaging.services;

import lombok.RequiredArgsConstructor;
import org.azotiques.sonantv1.guild.GuildApi;
import org.azotiques.sonantv1.messaging.dtos.MessageDto;
import org.azotiques.sonantv1.messaging.dtos.MessageRequest;
import org.azotiques.sonantv1.messaging.entities.Message;
import org.azotiques.sonantv1.messaging.exceptions.MessageNotFoundException;
import org.azotiques.sonantv1.messaging.mappers.MessageMapper;
import org.azotiques.sonantv1.messaging.repositories.MessageRepository;
import org.azotiques.sonantv1.storage.FileStorageApi;
import org.azotiques.sonantv1.user.UserApi;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageService {

    private static final long MAX_TOTAL_ATTACHMENT_SIZE = 10L * 1024 * 1024;

    private final MessageRepository messageRepository;
    private final GuildApi guildApi;
    private final UserApi userApi;
    private final MessageMapper messageMapper;
    private final FileStorageApi fileStorageApi;

    public List<String> uploadAttachments(Jwt jwt, Long guildId, Long channelId, MultipartFile[] files) {
        Long userId = userApi.requireId(jwt.getSubject());
        guildApi.findGuildByMemberAndIdAndChannelId(userId, guildId, channelId);
        guildApi.findChannelByMemberAndIdAndGuildId(userId, channelId, guildId);

        if (files == null || files.length == 0) {
            return List.of();
        }

        long totalSize = 0L;
        for (MultipartFile file : files) {
            if (file == null || file.isEmpty() || file.getContentType() == null) {
                throw new IllegalArgumentException("File must not be empty");
            }
            totalSize += file.getSize();
            if (totalSize > MAX_TOTAL_ATTACHMENT_SIZE) {
                throw new IllegalArgumentException("Attachments exceed 10MB limit");
            }
        }

        List<String> keys = new ArrayList<>(files.length);
        for (MultipartFile file : files) {
            keys.add(fileStorageApi.upload(file));
        }
        return keys;
    }

    public void deleteMessage(Jwt jwt, MessageRequest messageRequest, Long guildId, Long channelId) {
        Long userId = userApi.requireId(jwt.getSubject());

        Long guild = guildApi.findGuildByMemberAndIdAndChannelId(userId, guildId, channelId);
        Long channel = guildApi.findChannelByMemberAndIdAndGuildId(userId, channelId, guildId);

        Message message = messageRepository.findByIdAndGuildAndChannel(messageRequest.messageId(), guild, channel)
                .orElseThrow(() -> new MessageNotFoundException("Message not found"));

        if (message.getAttachments() != null) {
            for (String key : message.getAttachments()) {
                fileStorageApi.delete(key);
            }
        }

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

        return messageMapper.toMessageDtos(messages, userApi.findAllById(authorIds));
    }

}
