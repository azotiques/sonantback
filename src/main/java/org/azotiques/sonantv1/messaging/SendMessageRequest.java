package org.azotiques.sonantv1.messaging;

import org.azotiques.sonantv1.user.UserDto;

public record SendMessageRequest(
        String message,
        UserDto user,
        Long guildId,
        Long channelId
) {
}
