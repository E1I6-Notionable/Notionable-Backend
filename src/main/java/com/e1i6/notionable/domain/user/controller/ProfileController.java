package com.e1i6.notionable.domain.user.controller;

import com.e1i6.notionable.domain.user.data.dto.UserDto;
import com.e1i6.notionable.domain.user.data.dto.request.EmailLoginReqDto;
import com.e1i6.notionable.domain.user.data.dto.response.EmailLoginResDto;
import com.e1i6.notionable.domain.user.service.ProfileService;
import com.e1i6.notionable.global.auth.JwtProvider;
import com.e1i6.notionable.global.auth.JwtUtil;
import com.e1i6.notionable.global.common.response.BaseResponse;
import com.e1i6.notionable.global.common.response.ResponseCode;
import com.e1i6.notionable.global.common.response.ResponseException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class ProfileController {

    private final ProfileService profileService;
    private final JwtProvider jwtProvider;
    private final JwtUtil jwtUtil;

    // 마이페이지 - 내 정보 수정
    @GetMapping("/my-profile")
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

    @PatchMapping("/my-profile/modify")
    public BaseResponse<UserDto> modifyMyProfile(@RequestHeader("Authorization") String authorizationHeader,
                                                 @RequestBody UserDto modifyUserDto){
        try {
            // 헤더에서 JWT 토큰 추출
            String accessToken = authorizationHeader.replace("Bearer ", "");
            UserDto userIdDto = null;

            // 토큰 검증
            if (jwtProvider.validateToken(accessToken))
                userIdDto = jwtUtil.getUserFromToken(accessToken);


            return new BaseResponse<>(profileService.modifyMyProfile(userIdDto.getUserId(), modifyUserDto));
        } catch (ResponseException e) {
            return new BaseResponse<>(e.getErrorCode());
        } catch (Exception e) {
            return new BaseResponse<>(ResponseCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
