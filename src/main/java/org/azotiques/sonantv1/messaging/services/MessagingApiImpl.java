package org.azotiques.sonantv1.messaging.services;

import lombok.RequiredArgsConstructor;
import org.azotiques.sonantv1.messaging.MessagingApi;
import org.azotiques.sonantv1.messaging.SendMessageRequest;
import org.azotiques.sonantv1.messaging.SendMessageResponse;
import org.azotiques.sonantv1.messaging.entities.Message;
import org.azotiques.sonantv1.messaging.mappers.MessageMapper;
import org.azotiques.sonantv1.messaging.repositories.MessageRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessagingApiImpl implements MessagingApi {

    private final MessageRepository messageRepository;

    private final MessageMapper messageMapper;

    @Override
    public SendMessageResponse sendMessage(SendMessageRequest messageRequest) {
        Message message = new Message();
        message.setMessage(messageRequest.message());
        message.setGuild(Long.parseLong(messageRequest.guildId()));
        message.setChannel(Long.parseLong(messageRequest.channelId()));
        message.setUsers(Long.parseLong(messageRequest.user().id()));
        message.setAttachments(
                messageRequest.attachments() != null ? messageRequest.attachments() : List.of()
        );

        return messageMapper.toMessageResponse(messageRepository.save(message), messageRequest.user());
    }
}
