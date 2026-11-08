package org.azotiques.sonantv1.realtime.controllers;

import lombok.RequiredArgsConstructor;
import org.azotiques.sonantv1.realtime.dtos.PresenceDto;
import org.azotiques.sonantv1.realtime.services.PresenceService;
import org.azotiques.sonantv1.user.*;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class PresenceController {

    private final SimpMessagingTemplate simpMessagingTemplate;

    private final PresenceService presenceService;

    private final UserApi userApi;

    @MessageMapping("/presence/heartbeat")
    public void heartbeat(Principal principal, @Header("simpSessionId") String sessionId) {

        var auth = (UsernamePasswordAuthenticationToken) principal;
        Jwt jwt = (Jwt) auth.getPrincipal();
        UserDto user = userApi.findDtoBySub(jwt.getSubject());

        boolean becameOnline = presenceService.heartbeat(user.id(), sessionId);

        if (becameOnline) {
            broadcastToFriends(user.id(), UserStatus.ONLINE);
        }

        sendOnlineFriendsToUser(user.id());
    }

    @EventListener
    public void handleDisconnect(SessionDisconnectEvent event) {
        if (event.getUser() instanceof UsernamePasswordAuthenticationToken auth &&
        auth.getPrincipal() instanceof Jwt jwt) {
            UserDto user = userApi.findDtoBySub(jwt.getSubject());

            if (presenceService.markOffline(user.id(), event.getSessionId())) {
                broadcastToFriends(user.id(), UserStatus.OFFLINE);
            }
        }
    }

    private void broadcastToFriends(String userId, UserStatus status) {
        List<RelationshipDto> relationships = userApi.getAllFriendsByUserId(Long.parseLong(userId));

        PresenceDto payload = new PresenceDto(userId, status);

        for (RelationshipDto relationship : relationships) {
            String friendId =
                    userId.equals(relationship.incomingUser().id())
                    ? relationship.outgoingUser().id()
                    : relationship.incomingUser().id();

            simpMessagingTemplate.convertAndSend(
                    "/topic/presence." + friendId, payload
            );
        }

    }

    public void sendOnlineFriendsToUser(String userId) {
        List<RelationshipDto> relationships = userApi.getAllFriendsByUserId(Long.parseLong(userId));

        for (RelationshipDto relationship : relationships) {
            String friendId =
                    userId.equals(relationship.incomingUser().id())
                            ? relationship.outgoingUser().id()
                            : relationship.incomingUser().id();

            if ("ONLINE".equals(presenceService.getStatus(friendId))) {
                simpMessagingTemplate.convertAndSend(
                        "/topic/presence." + userId, new PresenceDto(friendId, UserStatus.ONLINE)
                );
            }
        }
    }
}
