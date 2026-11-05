package org.azotiques.sonantv1.guild.services;

import lombok.RequiredArgsConstructor;
import org.azotiques.sonantv1.user.RelationshipCreated;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DirectGuildListener {

    private final GuildService guildService;

    @EventListener
    void on (RelationshipCreated event) {
        guildService.createDirectMessagingChannel(event.outgoingUserId(), event.incomingUserId());
    }

}
