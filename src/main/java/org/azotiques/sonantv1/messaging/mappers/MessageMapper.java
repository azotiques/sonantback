package org.azotiques.sonantv1.messaging.mappers;

import lombok.RequiredArgsConstructor;
import org.azotiques.sonantv1.messaging.SendMessageResponse;
import org.azotiques.sonantv1.messaging.dtos.MessageDto;
import org.azotiques.sonantv1.messaging.entities.Message;
import org.azotiques.sonantv1.storage.FileStorageApi;
import org.azotiques.sonantv1.user.UserDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MessageMapper {

    private final FileStorageApi fileStorageApi;

    public MessageDto toMessageDto(Message message, Map<String, UserDto> users) {
        return new MessageDto(
                message.getId().toString(),
                message.getMessage(),
                users.get(message.getUsers().toString()),
                message.getChannel(),
                message.getGuild(),
                message.getCreatedAt(),
                toAttachmentUrls(message.getAttachments())
        );
    }

    public SendMessageResponse toMessageResponse(Message message, UserDto users) {
        return new SendMessageResponse(
                message.getId().toString(),
                message.getMessage(),
                users,
                message.getGuild().toString(),
                message.getChannel().toString(),
                message.getCreatedAt(),
                toAttachmentUrls(message.getAttachments())
        );
    }

    private List<String> toAttachmentUrls(List<String> keys) {
        if (keys == null || keys.isEmpty()) {
            return List.of();
        }
        return keys.stream()
                .map(fileStorageApi::getUrl)
                .toList();
    }

    public Message toMessage(MessageDto messageDto) {
        Message message = new Message();
        message.setMessage(messageDto.message());
        message.setChannel(messageDto.channelId());
        message.setGuild(messageDto.guildId());
        return message;
    }

    public List<MessageDto> toMessageDtos(List<Message> messages, Map<String, UserDto> users) {
        List<MessageDto> dtos = new ArrayList<MessageDto>();
        messages.forEach(message -> dtos.add(toMessageDto(message, users)));
        return dtos;
    }

}
