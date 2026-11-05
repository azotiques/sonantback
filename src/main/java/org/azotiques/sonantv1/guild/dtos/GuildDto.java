package org.azotiques.sonantv1.guild.dtos;

import java.util.List;
import java.util.Set;

public record GuildDto(
    String guildId,
    String name,
    String avatar,
    GuildType guildType,
    List<ChannelDto> channels,
    Set<Long> members
) {
}
