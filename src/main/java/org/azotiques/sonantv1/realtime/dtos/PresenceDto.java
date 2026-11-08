package org.azotiques.sonantv1.realtime.dtos;

import org.azotiques.sonantv1.user.UserStatus;

public record PresenceDto(
        String userId,
        UserStatus status
) {
}
