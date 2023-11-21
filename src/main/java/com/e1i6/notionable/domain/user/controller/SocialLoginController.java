package com.e1i6.notionable.domain.user.controller;

import com.e1i6.notionable.domain.user.data.dto.KakaoLoginDto;
import com.e1i6.notionable.domain.user.service.SocialLoginService;
import com.e1i6.notionable.global.common.response.BaseResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/login/oauth2")
public class SocialLoginController {
    private final SocialLoginService socialLoginService;
    @GetMapping(value = "/code/kakao", produces = "application/json")
    public BaseResponse<KakaoLoginDto> kakaoCallback(@RequestParam String code) throws JsonProcessingException {
        JsonNode accessTokenResponse = socialLoginService.getAccessTokenResponse(code); // code를 통해 얻은 response(access token과 여러 key들 존재)
        JsonNode userInfoResponse = socialLoginService.getUserInfoByAccessTokenResponse(accessTokenResponse); // access token을 통해 얻은 response(유저 정보 존재)
        KakaoLoginDto kakaoLoginDto = socialLoginService.kakaoLogin(userInfoResponse);
        return new BaseResponse<>(kakaoLoginDto);
    }
}
