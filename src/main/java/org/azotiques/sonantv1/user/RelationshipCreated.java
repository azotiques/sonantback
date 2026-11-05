package org.azotiques.sonantv1.user;

public record RelationshipCreated(
        Long relationshipId,
        Long outgoingUserId,
        Long incomingUserId
) {
}
