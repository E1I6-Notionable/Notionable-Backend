package com.e1i6.notionable.domain.user.controller;

import com.e1i6.notionable.domain.user.data.dto.request.SocialLoginReqDto;
import com.e1i6.notionable.domain.user.data.dto.response.SocialLoginResDto;
import com.e1i6.notionable.domain.user.service.SocialLoginService;
import com.e1i6.notionable.global.common.response.BaseResponse;
import com.e1i6.notionable.global.common.response.ResponseCode;
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

    @PostMapping("/social")
    public BaseResponse<SocialLoginResDto> socialLogin (
            @RequestBody SocialLoginReqDto reqDto) throws JsonProcessingException {
        log.info("code: {}", reqDto.getCode());
        try {
            SocialLoginResDto socialLoginResDto = socialLoginService.socialLogin(reqDto.getCode(), reqDto.getSocialType());
            return new BaseResponse<>(socialLoginResDto);
        } catch (Exception e) {
            return new BaseResponse<>(ResponseCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
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
