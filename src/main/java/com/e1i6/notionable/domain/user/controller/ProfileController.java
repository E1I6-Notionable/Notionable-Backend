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
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class ProfileController {

    private final ProfileService profileService;
    private final JwtProvider jwtProvider;
    private final JwtUtil jwtUtil;

    // 마이페이지 - 내 정보 조회
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

    // 마이페이지 - 내 정보 수정
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

    // 마이페이지 - 내가 쓴 글
    @GetMapping("/community")
    public BaseResponse<?> getCommunity(@RequestHeader("Authorization") String authorizationHeader,
                                        @PageableDefault(size = 5, sort = "createdAt",
                                                direction = Sort.Direction.DESC)
                                        Pageable pageable) {
        try {
            // 헤더에서 JWT 토큰 추출
            String accessToken = authorizationHeader.replace("Bearer ", "");
            UserDto userDto = null;

            // 토큰 검증
            if (jwtProvider.validateToken(accessToken))
                userDto = jwtUtil.getUserFromToken(accessToken);
            return new BaseResponse<>(profileService.getMyCommunity(userDto.getUserId(), pageable));
        } catch (ResponseException e) {
            return new BaseResponse<>(e.getErrorCode(), e.getMessage());
        }    catch (Exception e) {
            return new BaseResponse<>(ResponseCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

}
