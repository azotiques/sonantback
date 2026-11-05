package org.azotiques.sonantv1.user.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.azotiques.sonantv1.utils.SnowflakeId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@Table(name = "users")
public class User {

    @Id
    @SnowflakeId
    private Long id;

    @Column(unique = true, nullable = false)
    private String sub;

    private String username;

    private String avatar;

    @CreatedDate
    private LocalDateTime createdAt;

}
