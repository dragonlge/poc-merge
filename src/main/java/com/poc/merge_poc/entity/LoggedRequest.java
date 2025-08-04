package com.poc.merge_poc.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;


@Entity
@Table(name = "logged_requests")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoggedRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String method;

    private String uri;

    @Column(columnDefinition = "TEXT")
    private String headers;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "body", columnDefinition = "json")
    private String body;

    @Column(name = "processed")
    private Boolean processed;

}