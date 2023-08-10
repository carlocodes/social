package com.carlocodes.scoial.dtos;

import java.time.LocalDateTime;

public class Otp {
    private String value;
    private LocalDateTime expiration;

    public Otp(String value, LocalDateTime expiration) {
        this.value = value;
        this.expiration = expiration;
    }

    public String getValue() {
        return value;
    }

    public LocalDateTime getExpiration() {
        return expiration;
    }
}
