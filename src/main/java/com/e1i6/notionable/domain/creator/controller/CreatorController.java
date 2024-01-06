package com.e1i6.notionable.domain.creator.controller;

import com.e1i6.notionable.domain.creator.service.CreatorService;
import com.e1i6.notionable.domain.user.data.dto.UserDto;
import com.e1i6.notionable.global.common.response.BaseResponse;
import com.e1i6.notionable.global.common.response.ResponseCode;
import com.e1i6.notionable.global.common.response.ResponseException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class CreatorController {

    private final CreatorService creatorService;

    @PostMapping("/creator/register")
    public BaseResponse<UserDto> getMyProfile(@RequestHeader("Authorization") String authorizationHeader) {
        try {
            // 헤더에서 JWT 토큰 추출
            String accessToken = authorizationHeader.replace("Bearer ", "");
            UserDto userIdDto = null;

            // 토큰 검증
            if (jwtProvider.validateToken(accessToken))
                userIdDto = jwtUtil.getUserFromToken(accessToken);

            UserDto userDto = profileService.getMyProfile(userIdDto.getUserId());


            return new BaseResponse<>(userDto);
        } catch (ResponseException e) {
            return new BaseResponse<>(e.getErrorCode());
        } catch (Exception e) {
            return new BaseResponse<>(ResponseCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
