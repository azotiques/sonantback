package org.azotiques.sonantv1.user.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.azotiques.sonantv1.user.RelationshipCreated;
import org.azotiques.sonantv1.user.UserNotFoundException;
import org.azotiques.sonantv1.user.dtos.RelationshipDto;
import org.azotiques.sonantv1.user.dtos.RelationshipRequest;
import org.azotiques.sonantv1.user.entities.Relationship;
import org.azotiques.sonantv1.user.entities.User;
import org.azotiques.sonantv1.user.exceptions.RelationshipNotFoundException;
import org.azotiques.sonantv1.user.mappers.RelationshipMapper;
import org.azotiques.sonantv1.user.repositories.RelationshipRepository;
import org.azotiques.sonantv1.user.repositories.UserRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.azotiques.sonantv1.user.dtos.Status.FRIENDS;
import static org.azotiques.sonantv1.user.dtos.Status.PENDING;
import static org.azotiques.sonantv1.user.mappers.RelationshipMapper.toRelationshipDtos;

@Service
@RequiredArgsConstructor
public class RelationshipService {

    private final RelationshipRepository relationshipRepository;
    private final UserRepository userRepository;
    private final ApplicationEventPublisher events;

    public RelationshipDto updateRelationship(Jwt jwt, RelationshipRequest relationshipRequest) {
        User outgoingUser = userRepository.findBySub(jwt.getSubject()).orElseThrow(() -> new UserNotFoundException("Couldn't find user"));
        User incomingUser = userRepository.findByUsername(relationshipRequest.outgoingUser()).orElseThrow(() -> new UserNotFoundException("Couldn't find user"));
        Relationship relationship = relationshipRepository.findByIncomingUserAndOutgoingUser(outgoingUser, incomingUser).orElseThrow(() -> new RelationshipNotFoundException("Couldn't find relationship"));
        relationship.setStatus(FRIENDS);
        return RelationshipMapper.toRelationshipDto(relationshipRepository.save(relationship));
    }

    public List<RelationshipDto> getRelationshipFriends(Jwt jwt) {
        User user = userRepository.findBySub(jwt.getSubject()).orElseThrow(() -> new UserNotFoundException("Couldn't find user"));
        List<Relationship> relationship = relationshipRepository.findByOutgoingUserOrIncomingUserAndStatus(user, user, FRIENDS);
        return toRelationshipDtos(relationship);
    }

    public List<RelationshipDto> getRelationships(Jwt jwt) {
        User user = userRepository.findBySub(jwt.getSubject()).orElseThrow(() -> new UserNotFoundException("Couldn't find user"));
        List<Relationship> relationship = relationshipRepository.findByIncomingUser(user);
        return toRelationshipDtos(relationship);
    }

    @Transactional
    public void createRelationship(Jwt jwt, RelationshipRequest relationshipRequest) {

        User outgoingUser = userRepository.findBySub(jwt.getSubject())
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        User incomingUser = userRepository.findByUsername(relationshipRequest.outgoingUser())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Relationship relationship = new Relationship();

        relationship.setOutgoingUser(outgoingUser);
        relationship.setIncomingUser(incomingUser);
        relationship.setStatus(PENDING);

        events.publishEvent(new RelationshipCreated(outgoingUser.getId(), incomingUser.getId()));

        relationshipRepository.save(relationship);
    }

}
