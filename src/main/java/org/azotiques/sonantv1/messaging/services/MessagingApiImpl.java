package org.azotiques.sonantv1.messaging.services;

import lombok.RequiredArgsConstructor;
import org.azotiques.sonantv1.messaging.MessagingApi;
import org.azotiques.sonantv1.messaging.SendMessageRequest;
import org.azotiques.sonantv1.messaging.SendMessageResponse;
import org.azotiques.sonantv1.messaging.entities.Message;
import org.azotiques.sonantv1.messaging.mappers.MessageMapper;
import org.azotiques.sonantv1.messaging.repositories.MessageRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessagingApiImpl implements MessagingApi {

    private final MessageRepository messageRepository;

    @Override
    public SendMessageResponse sendMessage(SendMessageRequest messageRequest) {
        Message message = new Message();
        message.setMessage(messageRequest.message());
        message.setGuild(messageRequest.guildId());
        message.setChannel(messageRequest.channelId());
        message.setUsers(messageRequest.user().id());

        return MessageMapper.toMessageResponse(messageRepository.save(message), messageRequest.user());
    }
}
