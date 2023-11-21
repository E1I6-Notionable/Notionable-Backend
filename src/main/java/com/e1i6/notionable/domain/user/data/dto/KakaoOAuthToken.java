package com.e1i6.notionable.domain.user.data.dto;

import lombok.Data;

@Data
public class KakaoOAuthToken {
    private String access_token;
    private String token_type;
    private String refresh_token;
    private String expires_in;
    private String scope;
    private String refresh_token_expires_in;
}
