package org.azotiques.sonantv1.guild.dtos;

public record ChannelDto(
        String channelId,
        String name,
        ChannelType channelType
) {
}
