package org.azotiques.sonantv1.user;

import java.time.LocalDateTime;

public record UserDto(
        Long id,
        String username,
        String avatar,
        LocalDateTime createdAt
) {
}
