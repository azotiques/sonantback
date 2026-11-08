package org.azotiques.sonantv1.messaging.dtos;

import org.azotiques.sonantv1.user.UserDto;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

public record MessageDto(
        String id,
        String message,
        UserDto user,
        Long channelId,
        Long guildId,
        Instant createdAt,
        List<String> attachments
) {
}