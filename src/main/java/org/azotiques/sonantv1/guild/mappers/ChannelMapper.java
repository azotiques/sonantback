package org.azotiques.sonantv1.guild.mappers;

import org.azotiques.sonantv1.guild.dtos.ChannelDto;
import org.azotiques.sonantv1.guild.entities.Channel;

import java.util.ArrayList;
import java.util.List;

public class ChannelMapper {
    public static ChannelDto toChannelDto(Channel channel) {
        return new ChannelDto(channel.getId().toString(), channel.getName(), channel.getType());
    }

    public static List<ChannelDto> toChannelDtos(List<Channel> channels) {
        List<ChannelDto> channelDtos = new ArrayList<>();
        channels.forEach(channel -> channelDtos.add(toChannelDto(channel)));
        return channelDtos;
    }
}
