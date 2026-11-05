package org.azotiques.sonantv1.guild.mappers;

import org.azotiques.sonantv1.guild.dtos.GuildDto;
import org.azotiques.sonantv1.guild.entities.Guild;

import java.util.ArrayList;
import java.util.List;

public class GuildMapper {

    public static GuildDto toGuildDto(Guild guild) {
        return new GuildDto(guild.getId().toString(), guild.getName(), guild.getAvatar(), guild.getGuildType(), ChannelMapper.toChannelDtos(guild.getChannels()), guild.getMemberIds());
    }

    public static List<GuildDto> toGuildDtos(List<Guild> guilds) {
        List<GuildDto> guildDtos = new ArrayList<>();
        guilds.forEach(guild -> guildDtos.add(toGuildDto(guild)));
        return guildDtos;
    }

}
