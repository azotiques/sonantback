package org.azotiques.sonantv1.user.dtos;

import org.azotiques.sonantv1.user.UserDto;

public record RelationshipDto(
    Status status,
    UserDto incomingUser,
    UserDto outgoingUser,
    String guildId,
    String channelId
) {
}
