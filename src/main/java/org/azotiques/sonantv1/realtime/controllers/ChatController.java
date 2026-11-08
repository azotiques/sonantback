package org.azotiques.sonantv1.realtime.controllers;

import lombok.RequiredArgsConstructor;
import org.azotiques.sonantv1.messaging.SendMessageResponse;
import org.azotiques.sonantv1.realtime.dtos.ChatMessage;
import org.azotiques.sonantv1.realtime.dtos.TypingDto;
import org.azotiques.sonantv1.realtime.services.ChatService;
import org.azotiques.sonantv1.user.UserApi;
import org.azotiques.sonantv1.user.UserDto;
import org.azotiques.sonantv1.user.entities.User;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ChatService chatService;
    private final UserApi userApi;

    @MessageMapping("/chat/{guildId}/{channelId}")
    public void send(Principal principal, @Payload ChatMessage chatMessage, @DestinationVariable Long guildId, @DestinationVariable Long channelId) {

        var auth = (UsernamePasswordAuthenticationToken) principal;
        Jwt jwt = (Jwt) auth.getPrincipal();

        SendMessageResponse message = chatService.save(jwt, chatMessage, guildId, channelId);

        simpMessagingTemplate.convertAndSend("/topic/chat.guild." + guildId + "." + channelId, message);
    }

    @MessageMapping("/chat/{guildId}/{channelId}/typing")
    public void typing(Principal principal, @DestinationVariable Long guildId, @DestinationVariable Long channelId) {
        var auth = (UsernamePasswordAuthenticationToken) principal;
        Jwt jwt = (Jwt) auth.getPrincipal();

        UserDto user = userApi.findDtoBySub(jwt.getSubject());

        TypingDto payload = new TypingDto(user.id(), user.username());

        simpMessagingTemplate.convertAndSend("/topic/chat.guild." + guildId + "." + channelId + ".typing", payload);
    }

}
