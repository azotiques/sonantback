package org.azotiques.sonantv1.messaging.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.azotiques.sonantv1.utils.SnowflakeId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "messages")
@Getter
@Setter
public class Message {

    @Id
    @SnowflakeId
    private Long id;

    private String message;

    private Long users;

    private Long channel;


    private Long guild;

    @CreatedDate
    private Instant createdAt;

    @JdbcTypeCode(SqlTypes.ARRAY)
    private List<String> attachments = new ArrayList<>();

}
