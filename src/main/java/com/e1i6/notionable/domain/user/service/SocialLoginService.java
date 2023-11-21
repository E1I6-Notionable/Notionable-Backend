package com.e1i6.notionable.domain.user.service;

import com.e1i6.notionable.domain.user.data.dto.KakaoLoginDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

public interface SocialLoginService {
    JsonNode getAccessTokenResponse(String code) ;
    JsonNode getUserInfoByAccessTokenResponse(JsonNode accessToken) throws JsonProcessingException;
    KakaoLoginDto kakaoLogin(JsonNode userResourceNode) throws JsonProcessingException;
}
