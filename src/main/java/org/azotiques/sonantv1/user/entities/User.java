package org.azotiques.sonantv1.user.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.azotiques.sonantv1.user.dtos.PreferredUserStatus;
import org.azotiques.sonantv1.user.UserStatus;
import org.azotiques.sonantv1.utils.SnowflakeId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@Table(name = "users")
public class User {

    @Id
    @SnowflakeId
    private Long id;

    @Enumerated(EnumType.STRING)
    private PreferredUserStatus preferredUserStatus;

    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @Column(unique = true, nullable = false)
    private String sub;

    private Boolean accountSetup = false;

    private String username;

    private String avatar;

    @CreatedDate
    private LocalDateTime createdAt;

}
