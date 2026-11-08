package org.azotiques.sonantv1.user.controllers;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.azotiques.sonantv1.user.RelationshipDto;
import org.azotiques.sonantv1.user.RelationshipRequest;
import org.azotiques.sonantv1.user.services.RelationshipService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/")
public class RelationshipController {

    private final RelationshipService relationshipService;

    @PutMapping("/relationship")
    public ResponseEntity<RelationshipDto> updateRelationship(@AuthenticationPrincipal Jwt jwt, @RequestBody RelationshipRequest relationshipRequest) {
        return new ResponseEntity<>(relationshipService.updateRelationship(jwt, relationshipRequest), HttpStatus.OK);
    }

    @GetMapping("/relationship/friends")
    public ResponseEntity<List<RelationshipDto>> getRelationshipFriends(@AuthenticationPrincipal Jwt jwt) {
        return new ResponseEntity<>(relationshipService.getRelationshipFriends(jwt), HttpStatus.OK);
    }

    @GetMapping("/relationship")
    public ResponseEntity<List<RelationshipDto>> getRelationships(@AuthenticationPrincipal Jwt jwt) {
        return new ResponseEntity<>(relationshipService.getRelationships(jwt), HttpStatus.OK);
    }

    @PostMapping("/relationship/create")
    @Transactional
    @ResponseStatus(HttpStatus.CREATED)
    public void createRelationship(@AuthenticationPrincipal Jwt jwt, @RequestBody RelationshipRequest relationshipRequest) {
        relationshipService.createRelationship(jwt, relationshipRequest);
    }

}
