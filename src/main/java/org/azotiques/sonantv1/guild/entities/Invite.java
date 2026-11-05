package org.azotiques.sonantv1.guild.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.azotiques.sonantv1.guild.dtos.InviteType;
import org.azotiques.sonantv1.utils.SnowflakeId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class Invite {

    @Id
    @SnowflakeId
    private Long id;

    private String inviteCode;

    @ManyToOne(fetch = FetchType.LAZY)
    Guild guild;

    @Column(name = "user_id")
    private Long creatorId;

    @Enumerated(EnumType.STRING)
    private InviteType inviteType;

    @CreatedDate
    private LocalDateTime createdAt;

}
