package com.e1i6.notionable.domain.usersmsauth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.e1i6.notionable.domain.usersmsauth.dto.UserSMSAuthDto;
import com.e1i6.notionable.domain.usersmsauth.service.UserSMSAuthService;
import com.e1i6.notionable.global.common.response.BaseResponse;
import com.e1i6.notionable.global.common.response.ResponseCode;
import com.e1i6.notionable.global.common.response.ResponseException;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class UserSMSAuthController {

	@Autowired
	private UserSMSAuthService userSMSAuthService;

	// 문자 인증번호 전송 API
	@PostMapping("/signup/send-code")
	public BaseResponse<?> sendAuthCode(@RequestBody UserSMSAuthDto userSMSAuthDto) {

		try {
			UserSMSAuthDto resultSMSAuthDto = userSMSAuthService.sendAuthCode(userSMSAuthDto);
			return new BaseResponse<>(resultSMSAuthDto);
		} catch (ResponseException e) {
			return new BaseResponse<>(e.getErrorCode());
		} catch (Exception e) {
			return new BaseResponse<>(ResponseCode.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}
}
