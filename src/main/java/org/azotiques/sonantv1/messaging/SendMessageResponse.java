package org.azotiques.sonantv1.messaging;

import org.azotiques.sonantv1.user.UserDto;

import java.time.LocalDateTime;

public record SendMessageResponse(
        String id,
        String message,
        UserDto user,
        String guildId,
        String channelId,
        LocalDateTime createdAt
) {
}
