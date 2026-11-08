package org.azotiques.sonantv1.messaging;

import org.azotiques.sonantv1.user.UserDto;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

public record SendMessageResponse(
        String id,
        String message,
        UserDto user,
        String guildId,
        String channelId,
        Instant createdAt,
        List<String> attachments
) {
}
