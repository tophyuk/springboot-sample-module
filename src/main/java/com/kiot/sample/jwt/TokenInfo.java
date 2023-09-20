package com.kiot.sample.jwt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
public class TokenInfo {

    private String accessToken;
    private String refreshToken;

    public void refreshTokenUpdate(String refreshToken) {
        this.refreshToken = refreshToken;
    }

}
