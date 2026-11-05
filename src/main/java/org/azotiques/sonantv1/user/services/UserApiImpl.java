package org.azotiques.sonantv1.user.services;

import lombok.RequiredArgsConstructor;
import org.azotiques.sonantv1.user.UserApi;
import org.azotiques.sonantv1.user.UserDto;
import org.azotiques.sonantv1.user.UserNotFoundException;
import org.azotiques.sonantv1.user.entities.User;
import org.azotiques.sonantv1.user.mappers.UserMapper;
import org.azotiques.sonantv1.user.repositories.UserRepository;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserApiImpl implements UserApi {

    private final UserProvisioningService userProvisioningService;
    private final UserRepository userRepository;
    private final RelationshipService relationshipService;

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
                        UserMapper::toUserDto
                ));
    }

    @Override
    public UserDto findDtoBySub(String sub) {
        return UserMapper.toUserDto(userRepository.findBySub(sub).orElseThrow(UserNotFoundException::new));
    }

    @Override
    public void assignGuildToRelationship(Long relationshipId, Long guildId) {
        relationshipService.assignGuild(relationshipId, guildId);
    }

}
