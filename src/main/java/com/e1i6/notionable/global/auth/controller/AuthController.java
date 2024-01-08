package com.e1i6.notionable.global.auth.controller;

import com.e1i6.notionable.global.auth.JwtDto;
import com.e1i6.notionable.global.auth.JwtProvider;
import com.e1i6.notionable.global.common.response.BaseResponse;
import com.e1i6.notionable.global.common.response.ResponseCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Slf4j
public class AuthController {

    private final JwtProvider jwtProvider;

    @GetMapping("/not-secured")
    public BaseResponse<ResponseCode> unauthorizedException() {
        return new BaseResponse<>(ResponseCode.UNAUTHORIZED);
    }

    @GetMapping("/denied")
    public BaseResponse<ResponseCode> forbiddenException() {
        return new BaseResponse<>(ResponseCode.FORBIDDEN);
    }

    @PostMapping("/reissue")
    public BaseResponse<JwtDto> reissueJwt(HttpServletRequest request) {
        String refreshToken = request.getHeader("Refresh-Token");
        log.info("refresh token: {}", refreshToken);
        try {
            if (StringUtils.hasText(refreshToken) && jwtProvider.validateToken(refreshToken)) {
                log.info("debug");
                Long userId = jwtProvider.getUserIdFromToken(refreshToken);
                log.info("debug2");
                return new BaseResponse<>(jwtProvider.generateToken(userId));
            }
            return new BaseResponse<>(ResponseCode.FORBIDDEN);
        } catch (Exception e) {
            return new BaseResponse<>(ResponseCode.FORBIDDEN);
        }
    }
}
