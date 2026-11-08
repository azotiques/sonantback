package org.azotiques.sonantv1.realtime.controllers;

import lombok.RequiredArgsConstructor;
import org.azotiques.sonantv1.user.RelationshipDto;
import org.azotiques.sonantv1.user.RelationshipRequest;
import org.azotiques.sonantv1.user.UserApi;
import org.azotiques.sonantv1.user.UserDto;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class FriendsController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final UserApi userApi;

    @MessageMapping("/relationships")
    public void send(Principal principal, @Payload RelationshipRequest request) {

        var auth = (UsernamePasswordAuthenticationToken) principal;
        Jwt jwt = (Jwt) auth.getPrincipal();

        RelationshipDto relationship = userApi.saveRelationship(jwt, request);

        simpMessagingTemplate.convertAndSend("/topic/relationships." + relationship.incomingUser().id(), relationship);
    }

    @MessageMapping("/relationships/friends")
    public void all(Principal principal) {
        var auth = (UsernamePasswordAuthenticationToken) principal;
        Jwt jwt = (Jwt) auth.getPrincipal();

        List<RelationshipDto> friends = userApi.getAllFriends(jwt);
        UserDto user = userApi.findDtoBySub(jwt.getSubject());

        simpMessagingTemplate.convertAndSend("/topic/relationships.friends." + user.id(), friends);
    }

    @MessageMapping("/relationships/accept")
    public void accept(Principal principal, @Payload RelationshipRequest request) {
        var auth = (UsernamePasswordAuthenticationToken) principal;
        Jwt jwt = (Jwt) auth.getPrincipal();

        RelationshipDto accepted = userApi.acceptRelationship(jwt, request);

        String incomingId = accepted.incomingUser().id();
        String outgoingId = accepted.outgoingUser().id();

        simpMessagingTemplate.convertAndSend(
                "/topic/relationships.friends." + incomingId,
                userApi.getAllFriendsByUserId(Long.parseLong(incomingId))
        );
        simpMessagingTemplate.convertAndSend(
                "/topic/relationships.friends." + outgoingId,
                userApi.getAllFriendsByUserId(Long.parseLong(outgoingId))
        );
    }
}