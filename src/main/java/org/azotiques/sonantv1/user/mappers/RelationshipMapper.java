package org.azotiques.sonantv1.user.mappers;

import lombok.RequiredArgsConstructor;
import org.azotiques.sonantv1.user.dtos.RelationshipDto;
import org.azotiques.sonantv1.user.entities.Relationship;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class RelationshipMapper {

    private final UserMapper userMapper;

    public RelationshipDto toRelationshipDto(Relationship relationship) {
        return new RelationshipDto(
                relationship.getStatus(),
                userMapper.toUserDto(relationship.getIncomingUser()),
                userMapper.toUserDto(relationship.getOutgoingUser()),
                relationship.getGuildId().toString(),
                relationship.getChannelId().toString()
        );
    }

    public List<RelationshipDto> toRelationshipDtos(List<Relationship> relationships) {
        List<RelationshipDto> dtos = new ArrayList<>();
        relationships.forEach(relationship -> dtos.add(toRelationshipDto(relationship)));
        return dtos;
    }
}
