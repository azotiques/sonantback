package org.azotiques.sonantv1.guild.services;

import lombok.RequiredArgsConstructor;
import org.azotiques.sonantv1.guild.entities.Guild;
import org.azotiques.sonantv1.user.RelationshipCreated;
import org.azotiques.sonantv1.user.UserApi;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DirectGuildListener {

    private final GuildService guildService;
    private final UserApi userApi;

    @EventListener
    void on (RelationshipCreated event) {
        Guild guild = guildService.createDirectMessagingChannel(event.outgoingUserId(), event.incomingUserId());
        userApi.assignGuildToRelationship(event.relationshipId(), guild.getId(), guild.getChannels().getFirst().getId());
    }

}
