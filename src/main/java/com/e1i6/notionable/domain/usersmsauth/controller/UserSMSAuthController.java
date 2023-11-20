package com.e1i6.notionable.domain.usersmsauth.controller;

import com.e1i6.notionable.domain.user.data.dto.response.EmailLoginResDto;
import com.e1i6.notionable.domain.usersmsauth.dto.UserSMSAuthDto;
import com.e1i6.notionable.domain.usersmsauth.service.UserSMSAuthService;
import com.e1i6.notionable.global.common.response.BaseResponse;
import com.e1i6.notionable.global.common.response.ResponseCode;
import com.e1i6.notionable.global.common.response.ResponseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserSMSAuthController {

    @Autowired
    private UserSMSAuthService userSMSAuthService;

    // 문자 인증번호 전송 API
    @PostMapping("/users/signup/send-code")
    public BaseResponse<?> sendAuthCode(@RequestBody UserSMSAuthDto userSMSAuthDto) {

        try {
            UserSMSAuthDto resultSMSAuthDto = userSMSAuthService.sendAuthCode(userSMSAuthDto);
            return new BaseResponse<>(resultSMSAuthDto);
        } catch (ResponseException e) {
            return new BaseResponse<>(e.getErrorCode());
        } catch (Exception e) {
            return new BaseResponse<>(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }
}