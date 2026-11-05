package org.azotiques.sonantv1.user.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.azotiques.sonantv1.user.dtos.Status;
import org.azotiques.sonantv1.utils.SnowflakeId;

@Entity
@Table(
        name = "relationships",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_relationships_outgoing_incoming",
                columnNames = {"outgoing_user_id", "incoming_user_id"}
        )
)
@Getter
@Setter
public class Relationship {

    @Id
    @SnowflakeId
    private Long id;

    @Enumerated(EnumType.STRING)
    private Status status;

    private Long guildId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "outgoing_user_id", nullable = false)
    private User outgoingUser;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "incoming_user_id", nullable = false)
    private User incomingUser;

}
