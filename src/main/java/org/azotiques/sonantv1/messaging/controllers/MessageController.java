package org.azotiques.sonantv1.messaging.controllers;

import lombok.RequiredArgsConstructor;
import org.azotiques.sonantv1.messaging.dtos.MessageDto;
import org.azotiques.sonantv1.messaging.dtos.MessageRequest;
import org.azotiques.sonantv1.messaging.services.MessageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @PostMapping(value = "/messages/{guildId}/{channelId}/attachments", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<String>> uploadAttachments(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long guildId,
            @PathVariable Long channelId,
            @RequestParam("attachments") MultipartFile[] attachments
    ) {
        return new ResponseEntity<>(messageService.uploadAttachments(jwt, guildId, channelId, attachments), HttpStatus.CREATED);
    }

    @DeleteMapping("/messages/{guildId}/{channelId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMessage(@AuthenticationPrincipal Jwt jwt, @RequestBody MessageRequest messageRequest, @PathVariable Long guildId, @PathVariable Long channelId) {
        messageService.deleteMessage(jwt, messageRequest, guildId, channelId);
    }

    @GetMapping("/messages/{guildId}/{channelId}")
    public ResponseEntity<List<MessageDto>> getMessages(@AuthenticationPrincipal Jwt jwt, @PathVariable Long guildId, @PathVariable Long channelId) {
        return new ResponseEntity<>(messageService.getMessages(jwt, guildId, channelId), HttpStatus.OK);
    }
}
