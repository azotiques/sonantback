package org.azotiques.sonantv1.user.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.azotiques.sonantv1.user.*;
import org.azotiques.sonantv1.user.dtos.PreferredUserStatus;
import org.azotiques.sonantv1.user.entities.Relationship;
import org.azotiques.sonantv1.user.entities.User;
import org.azotiques.sonantv1.user.mappers.RelationshipMapper;
import org.azotiques.sonantv1.user.mappers.UserMapper;
import org.azotiques.sonantv1.user.repositories.RelationshipRepository;
import org.azotiques.sonantv1.user.repositories.UserRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.azotiques.sonantv1.user.dtos.Status.PENDING;

@Service
@RequiredArgsConstructor
public class UserApiImpl implements UserApi {

    private final UserProvisioningService userProvisioningService;
    private final UserRepository userRepository;
    private final RelationshipService relationshipService;
    private final RelationshipRepository relationshipRepository;
    private final UserMapper userMapper;
    private final ApplicationEventPublisher events;
    private final RelationshipMapper relationshipMapper;

    @Override
    public Long provision(Jwt jwt) {
        User user = userProvisioningService.save(jwt);
        return user.getId();
    }

    @Override
    public Long requireId(String sub) {
        return userRepository.findBySub(sub)
                .orElseThrow(UserNotFoundException::new)
                .getId();
    }

    @Override
    public Optional<Long> findIdByUsername(String username) {
        return Optional.of(userRepository.findByUsername(username)
                .orElseThrow(UserNotFoundException::new)
                .getId());
    }

    @Override
    public Map<String, UserDto> findAllById(Collection<Long> ids) {
        return userRepository.findAllById(ids)
                .stream()
                .collect(Collectors.toMap(
                        user -> user.getId().toString(),
                        userMapper::toUserDto
                ));
    }

    @Override
    public UserDto findDtoBySub(String sub) {
        return userMapper.toUserDto(userRepository.findBySub(sub).orElseThrow(UserNotFoundException::new));
    }

    @Override
    public void assignGuildToRelationship(Long relationshipId, Long guildId, Long channelId) {
        relationshipService.assignGuild(relationshipId, guildId, channelId);
    }

    @Override
    @Transactional
    public RelationshipDto saveRelationship(Jwt jwt, RelationshipRequest request) {
        User outgoingUser = userRepository.findBySub(jwt.getSubject())
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        User incomingUser = userRepository.findByUsername(request.outgoingUser())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Relationship relationship = new Relationship();

        relationship.setOutgoingUser(outgoingUser);
        relationship.setIncomingUser(incomingUser);
        relationship.setStatus(PENDING);

        relationship = relationshipRepository.save(relationship);

        events.publishEvent(new RelationshipCreated(relationship.getId(), outgoingUser.getId(), incomingUser.getId()));

        return relationshipMapper.toRelationshipDto(relationship);
    }

    @Override
    @Transactional
    public RelationshipDto acceptRelationship(Jwt jwt, RelationshipRequest request) {
        return relationshipService.updateRelationship(jwt, request);
    }

    @Override
    @Transactional
    public UserDto setUserStatus(Jwt jwt, UserStatus status) {
        User user = userRepository.findBySub(jwt.getSubject())
                .orElseThrow(UserNotFoundException::new);

        user.setStatus(status);

        return userMapper.toUserDto(userRepository.save(user));
    }

    @Override
    public List<RelationshipDto> getAllFriends(Jwt jwt) {
        return relationshipService.getRelationshipFriends(jwt);
    }

    @Override
    public List<RelationshipDto> getAllFriendsByUserId(Long userId) {
        return relationshipService.getRelationshipFriendsByUserId(userId);
    }
}