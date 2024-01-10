package com.e1i6.notionable.domain.creator.controller;

import com.e1i6.notionable.domain.cart.dto.CartDto;
import com.e1i6.notionable.domain.creator.dto.CreatorDto;
import com.e1i6.notionable.domain.creator.service.CreatorService;
import com.e1i6.notionable.domain.user.data.dto.UserDto;
import com.e1i6.notionable.global.auth.JwtProvider;
import com.e1i6.notionable.global.auth.JwtUtil;
import com.e1i6.notionable.global.common.response.BaseResponse;
import com.e1i6.notionable.global.common.response.ResponseCode;
import com.e1i6.notionable.global.common.response.ResponseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@Slf4j
public class CreatorController {

    private final CreatorService creatorService;
    private final JwtProvider jwtProvider;
    private final JwtUtil jwtUtil;

    // 크리에이터 등록
    @PostMapping("/creator/register")
    public BaseResponse<?> creatorRegister(@RequestHeader("Authorization") String authorizationHeader,
                                           @RequestPart CreatorDto creatorDto,
                                           @RequestPart("bankPaper")MultipartFile bankPaper,
                                           @RequestPart("identification")MultipartFile identification) {

            // 헤더에서 JWT 토큰 추출
            String accessToken = authorizationHeader.replace("Bearer ", "");
            UserDto userIdDto = null;

            // 토큰 검증
            if (jwtProvider.validateToken(accessToken))
                userIdDto = jwtUtil.getUserFromToken(accessToken);

            CreatorDto returnCreatorDto = creatorService.creatorRegister(userIdDto.getUserId(), creatorDto, bankPaper, identification);

            if (returnCreatorDto == null)
                return new BaseResponse<>("이미 크리에이터를 신청하셨습니다. 승인 대기 상태입니다.");
            else
                return new BaseResponse<>(returnCreatorDto);
    }
}
