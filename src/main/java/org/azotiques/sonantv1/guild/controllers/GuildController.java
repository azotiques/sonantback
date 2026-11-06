package org.azotiques.sonantv1.guild.controllers;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.azotiques.sonantv1.guild.dtos.*;
import org.azotiques.sonantv1.guild.services.GuildService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/")
public class GuildController {

    private final GuildService guildService;

    @DeleteMapping("/guild/{guildId}/{channelId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteChannel(@AuthenticationPrincipal Jwt jwt, @PathVariable("guildId") Long guildId, @PathVariable("channelId") Long channelId) {
        guildService.deleteChannel(jwt, guildId, channelId);
    }

    @DeleteMapping("/guild/{guildId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteGuild(@AuthenticationPrincipal Jwt jwt, @PathVariable("guildId") Long guildId) {
        guildService.deleteGuild(jwt, guildId);
    }

    @PutMapping("/guild/{guildId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateGuild(@AuthenticationPrincipal Jwt jwt, @PathVariable("guildId") Long guildId, @RequestBody GuildRequest guildRequest) {
        guildService.updateGuild(jwt, guildId, guildRequest);
    }

    @PostMapping("/guild/invite/{inviteCode}")
    @Transactional
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void invite(@AuthenticationPrincipal Jwt jwt, @PathVariable String inviteCode) {
        guildService.invite(jwt, inviteCode);
    }

    @PostMapping("/guild/{guildId}/invite")
    public ResponseEntity<InviteResponse> invite(@AuthenticationPrincipal Jwt jwt, @PathVariable Long guildId, @RequestBody InviteRequest inviteRequest) {
        return new ResponseEntity<>(guildService.invite(jwt, guildId, inviteRequest), HttpStatus.CREATED);
    }

    @PostMapping("/guild/{guildId}")
    public ResponseEntity<ChannelDto> createChannel(@AuthenticationPrincipal Jwt jwt, @PathVariable Long guildId, @RequestBody ChannelDto channelDto) {
        return new ResponseEntity<>(guildService.createChannel(jwt, guildId, channelDto), HttpStatus.CREATED);
    }

    @GetMapping("/guild/{guildId}")
    public ResponseEntity<GuildDto> getGuild(@AuthenticationPrincipal Jwt jwt, @PathVariable("guildId") Long guildId) {
        return new ResponseEntity<>(guildService.getGuild(jwt, guildId), HttpStatus.OK);
    }

    @GetMapping("/guild")
    public ResponseEntity<List<GuildDto>> getGuilds(@AuthenticationPrincipal Jwt jwt) {
        return new ResponseEntity<>(guildService.getGuilds(jwt), HttpStatus.OK);
    }

    @PostMapping("/guild")
    @Transactional
    public ResponseEntity<GuildDto> createGuild(@AuthenticationPrincipal Jwt jwt, @RequestBody GuildRequest guildRequest) {
        return new ResponseEntity<>(guildService.createGuild(jwt, guildRequest), HttpStatus.CREATED);
    }
}