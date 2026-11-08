package org.azotiques.sonantv1.user;

import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UserApi {
    Long provision(Jwt jwt);
    Long requireId(String sub);
    Optional<Long> findIdByUsername(String username);
    Map<String, UserDto> findAllById(Collection<Long> ids);
    UserDto findDtoBySub(String sub);
    void assignGuildToRelationship(Long relationshipId, Long guildId, Long channelId);
    RelationshipDto saveRelationship(Jwt jwt, RelationshipRequest request);
    RelationshipDto acceptRelationship(Jwt jwt, RelationshipRequest request);
    UserDto setUserStatus(Jwt jwt, UserStatus status);
    List<RelationshipDto> getAllFriends(Jwt jwt);
    List<RelationshipDto> getAllFriendsByUserId(Long userId);
}