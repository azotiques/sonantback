package org.azotiques.sonantv1.messaging;

import org.azotiques.sonantv1.user.UserDto;

import java.util.List;

public record SendMessageRequest(
        String message,
        UserDto user,
        String guildId,
        String channelId,
        List<String> attachments
) {
}
