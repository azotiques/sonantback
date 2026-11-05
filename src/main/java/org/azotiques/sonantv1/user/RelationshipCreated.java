package org.azotiques.sonantv1.user;

public record RelationshipCreated(
        Long outgoingUserId,
        Long incomingUserId
) {
}
