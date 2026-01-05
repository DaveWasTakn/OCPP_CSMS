package com.dave.Main.Pv.Enphase;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
public class ApiToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 2048)
    private String token;

    private Instant timestamp;

    public ApiToken(String token, Instant timestamp) {
        this.token = token;
        this.timestamp = timestamp;
    }

    public ApiToken() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

}
