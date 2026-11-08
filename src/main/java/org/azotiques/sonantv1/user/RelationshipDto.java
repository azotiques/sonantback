package org.azotiques.sonantv1.user;

import org.azotiques.sonantv1.user.dtos.Status;

public record RelationshipDto(
    Status status,
    UserDto incomingUser,
    UserDto outgoingUser,
    String guildId,
    String channelId
) {
}
