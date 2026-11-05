package org.azotiques.sonantv1.guild.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.azotiques.sonantv1.guild.dtos.GuildType;
import org.azotiques.sonantv1.utils.SnowflakeId;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "guilds")
public class Guild {

    @Id
    @SnowflakeId
    private Long id;

    private String name;

    private String avatar;

    @OneToMany(mappedBy = "guild", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Channel> channels = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "guild_members", joinColumns = @JoinColumn(name = "guild_id"))
    @Column(name = "user_id")
    private Set<Long> memberIds = new HashSet<>();

    @OneToMany(mappedBy = "guild", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Invite> invite;

    @Enumerated(EnumType.STRING)
    private GuildType guildType;

    public void setChannels(List<Channel> channels) {
        this.channels.clear();
        if (channels != null) {
            channels.forEach(this::addChannel);
        }
    }

    public void addChannel(Channel channel) {
        channels.add(channel);
        channel.setGuild(this);
    }
}
