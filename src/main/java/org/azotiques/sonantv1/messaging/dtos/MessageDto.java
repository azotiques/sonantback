package org.azotiques.sonantv1.messaging.dtos;

import org.azotiques.sonantv1.user.UserDto;

import java.time.LocalDateTime;

public record MessageDto(
        String id,
        String message,
        UserDto user,
        Long channelId,
        Long guildId,
        LocalDateTime createdAt
) {
}