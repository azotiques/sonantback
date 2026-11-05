package org.azotiques.sonantv1.guild.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.azotiques.sonantv1.guild.dtos.ChannelType;
import org.azotiques.sonantv1.utils.SnowflakeId;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "channels")
@Getter
@Setter
public class Channel {

    @Id
    @SnowflakeId
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "guild_id", nullable = false)
    private Guild guild;

    private String name;

    @Enumerated(EnumType.STRING)
    private ChannelType type;

    @ElementCollection
    @CollectionTable(name = "channel_users", joinColumns = @JoinColumn(name = "channel_id"))
    @Column(name = "user_id")
    private Set<Long> userIds = new HashSet<>();

}
