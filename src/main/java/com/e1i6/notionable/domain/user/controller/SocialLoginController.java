package com.e1i6.notionable.domain.user.controller;

import com.e1i6.notionable.domain.user.data.dto.response.SocialLoginResDto;
import com.e1i6.notionable.domain.user.service.SocialLoginService;
import com.e1i6.notionable.global.common.response.BaseResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/login/oauth2")
public class SocialLoginController {
    private final SocialLoginService socialLoginService;

    @GetMapping("")
    public BaseResponse<String> getAllCommunity() {
        log.info("여기왔나?");
        return new BaseResponse<>("성공했습니다");
    }

    @GetMapping(value = "/code/kakao", produces = "application/json")
    public BaseResponse<SocialLoginResDto> kakaoCallback(@RequestParam String code) throws JsonProcessingException {
        SocialLoginResDto socialLoginResDto = socialLoginService.socialLogin(code,"kakao");
        return new BaseResponse<>(socialLoginResDto);
    }

    @GetMapping(value = "/code/naver")
    public BaseResponse<SocialLoginResDto> naverCallback(@RequestParam String code) throws JsonProcessingException {
        SocialLoginResDto socialLoginResDto = socialLoginService.socialLogin(code,"naver");
        return new BaseResponse<>(socialLoginResDto);
    }

    @GetMapping(value = "/code/google", produces = "application/json")
    public BaseResponse<SocialLoginResDto> googleCallback(@RequestParam String code) throws JsonProcessingException {
        SocialLoginResDto socialLoginResDto = socialLoginService.socialLogin(code,"google");
        return new BaseResponse<>(socialLoginResDto);
    }
}
