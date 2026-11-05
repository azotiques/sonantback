package org.azotiques.sonantv1.guild.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.azotiques.sonantv1.guild.dtos.*;
import org.azotiques.sonantv1.guild.entities.Channel;
import org.azotiques.sonantv1.guild.entities.Guild;
import org.azotiques.sonantv1.guild.entities.Invite;
import org.azotiques.sonantv1.guild.exceptions.*;
import org.azotiques.sonantv1.guild.mappers.ChannelMapper;
import org.azotiques.sonantv1.guild.mappers.GuildMapper;
import org.azotiques.sonantv1.guild.repositories.ChannelRepository;
import org.azotiques.sonantv1.guild.repositories.GuildRepository;
import org.azotiques.sonantv1.guild.repositories.InviteRepository;
import org.azotiques.sonantv1.user.UserApi;
import org.azotiques.sonantv1.utils.InviteCodeGenerator;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.azotiques.sonantv1.guild.dtos.ChannelType.MESSAGE;
import static org.azotiques.sonantv1.guild.dtos.GuildType.DM;
import static org.azotiques.sonantv1.guild.dtos.GuildType.GUILD;

@Service
@RequiredArgsConstructor
public class GuildService {

    private final GuildRepository guildRepository;
    private final ChannelRepository channelRepository;
    private final UserApi userApi;
    private final InviteRepository inviteRepository;

    public void deleteChannel(Jwt jwt, Long guildId, Long channelId) {
        Long userId = userApi.requireId(jwt.getSubject());

        Guild guild = guildRepository.findByIdAndMember(guildId, userId).orElseThrow(() -> new GuildNotFoundException("Guild not found"));

        Channel channel = channelRepository.findByUserAndIdAndGuild(userId, channelId, guild).orElseThrow(() -> new ChannelNotFoundException("Channel not found"));

        if (channelRepository.countByGuild(guild) <= 1) {
            throw new LastChannelException("Can't delete last channel in a guild");
        }

        channelRepository.delete(channel);
    }

    public void deleteGuild(Jwt jwt, Long guildId) {
        Long userId = userApi.requireId(jwt.getSubject());

        Guild guild = guildRepository.findByIdAndMember(guildId, userId).orElseThrow(() -> new GuildNotFoundException("Guild not found"));

        guildRepository.delete(guild);
    }

    public void updateGuild(Jwt jwt, Long guildId, GuildRequest guildRequest) {
        Long userId = userApi.requireId(jwt.getSubject());

        Guild guild = guildRepository.findByIdAndMember(guildId, userId).orElseThrow(() -> new GuildNotFoundException("Guild not found"));

        guild.setName(guildRequest.name());

        guildRepository.save(guild);
    }

    @Transactional
    public void invite(Jwt jwt, String inviteCode) {
        Long userId = userApi.requireId(jwt.getSubject());

        Invite invite = inviteRepository.findByInviteCode(inviteCode).orElseThrow(() -> new InviteNotFoundException("Invite code not found"));

        LocalDateTime dateCreated = invite.getCreatedAt();

        if (
                invite.getInviteType() == InviteType.EXPIRING
                        && dateCreated.isBefore(LocalDateTime.now().minusDays(30))
        ) {
            throw new InvalidInviteException("Invite expired");
        }

        Guild guild = invite.getGuild();

        if (guild.getMemberIds().stream().anyMatch(
                member -> member.equals(userId)
        )) {
            throw new InvalidUserInviteException("User is already invited");
        }

        guild.getMemberIds().add(userId);

        guild.getChannels().forEach(channel -> {
            channel.getUserIds().add(userId);
        });

        guildRepository.save(guild);
    }

    public InviteResponse invite(Jwt jwt, Long guildId, InviteRequest inviteRequest) {
        Long userId = userApi.requireId(jwt.getSubject());
        String code = InviteCodeGenerator.generate();
        Invite invite = new Invite();

        invite.setCreatorId(userId);
        invite.setInviteCode(code);
        invite.setInviteType(inviteRequest.inviteType());
        invite.setGuild(guildRepository.findById(guildId).orElseThrow(() -> new GuildNotFoundException("Guild not found")));
        inviteRepository.save(invite);

        return new InviteResponse(invite.getInviteCode());
    }

    public ChannelDto createChannel(Jwt jwt, Long guildId, ChannelDto channelDto) {
        Long userId = userApi.requireId(jwt.getSubject());
        Channel channel = new Channel();
        Guild guild = guildRepository.findByIdAndMember(guildId, userId).orElseThrow(() -> new GuildNotFoundException("guild not found"));

        channel.setGuild(guild);
        channel.setUserIds(new HashSet<>(guild.getMemberIds()));
        channel.setName(channelDto.name());
        channel.setType(channelDto.channelType());

        return ChannelMapper.toChannelDto(channelRepository.save(channel));
    }

    public GuildDto getGuild(Jwt jwt, Long guildId) {
        Long userId = userApi.requireId(jwt.getSubject());

        return GuildMapper.toGuildDto(guildRepository.findByIdAndMember(guildId, userId)
                .orElseThrow(() -> new GuildNotFoundException("Guild not found")));
    }

    public List<GuildDto> getGuilds(Jwt jwt) {
        Long userId = userApi.requireId(jwt.getSubject());

        return GuildMapper.toGuildDtos(guildRepository.findByMember(userId));
    }

    @Transactional
    public GuildDto createGuild(Jwt jwt, GuildRequest guildRequest) {
        Long userId = userApi.requireId(jwt.getSubject());

        Guild guild = new Guild();
        guild.setMemberIds(Set.of(userId));
        guild.setGuildType(GUILD);
        guild.setName(guildRequest.name());

        Channel channel = new Channel();
        channel.setGuild(guild);
        channel.setName("general");
        channel.setType(MESSAGE);
        channel.setUserIds(Set.of(userId));

        guild.setChannels(List.of(channel));
        guildRepository.save(guild);

        return GuildMapper.toGuildDto(guild);
    }


    @Transactional
    public Guild createDirectMessagingChannel(Long userId1, Long userId2) {
        Guild guild = new Guild();
        guild.setMemberIds(Set.of(userId1, userId2));
        guild.setGuildType(DM);
        Channel channel = new Channel();
        channel.setName("dm");
        channel.setType(MESSAGE);
        channel.setUserIds(Set.of(userId1, userId2));
        guild.addChannel(channel);
        return guildRepository.save(guild);
    }

}
