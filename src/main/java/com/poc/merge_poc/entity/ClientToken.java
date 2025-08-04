package com.poc.merge_poc.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "client_tokens")
@Data
@NoArgsConstructor
public class ClientToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "client_id", nullable = false)
    private String clientId;

    @Column(name = "client_name", nullable = false)
    private String clientName;

    @Column(name = "access_token", nullable = false)
    private String accessToken;


    public ClientToken(String clientId, String clientName, String accessToken) {
        this.clientId = clientId;
        this.clientName = clientName;
        this.accessToken = accessToken;
    }
}
