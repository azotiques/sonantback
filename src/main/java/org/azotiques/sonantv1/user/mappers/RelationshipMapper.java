package org.azotiques.sonantv1.user.mappers;

import org.azotiques.sonantv1.user.dtos.RelationshipDto;
import org.azotiques.sonantv1.user.entities.Relationship;

import java.util.ArrayList;
import java.util.List;

public class RelationshipMapper {

    static public RelationshipDto toRelationshipDto(Relationship relationship) {
        return new RelationshipDto(
                relationship.getStatus(),
                UserMapper.toUserDto(relationship.getIncomingUser()),
                UserMapper.toUserDto(relationship.getOutgoingUser()),
                relationship.getGuildId().toString(),
                relationship.getChannelId().toString()
        );
    }

    static public List<RelationshipDto> toRelationshipDtos(List<Relationship> relationships) {
        List<RelationshipDto> dtos = new ArrayList<>();
        relationships.forEach(relationship -> dtos.add(toRelationshipDto(relationship)));
        return dtos;
    }
}
