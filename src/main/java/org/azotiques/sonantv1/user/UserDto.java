package org.azotiques.sonantv1.user;

import org.azotiques.sonantv1.user.dtos.PreferredUserStatus;

import java.time.LocalDateTime;

public record UserDto(
        String id,
        UserStatus status,
        Boolean accountSetup,
        String username,
        String avatar,
        LocalDateTime createdAt
) {
}