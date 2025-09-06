package com.szte.SkyScope.Models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AmadeusApiCred {
    private String access_token;
    private String expires_in;

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(String expires_in) {
        this.expires_in = expires_in;
    }

    public boolean isExpired() {
        LocalDateTime expire =  LocalDateTime.now().minusSeconds(Integer.parseInt(expires_in));
        return expire.isBefore(LocalDateTime.now());
    }
}