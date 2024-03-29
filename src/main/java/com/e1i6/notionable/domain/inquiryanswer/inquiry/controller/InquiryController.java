package com.e1i6.notionable.domain.inquiryanswer.inquiry.controller;

import com.e1i6.notionable.domain.inquiryanswer.InquiryAnswerDto;
import com.e1i6.notionable.domain.inquiryanswer.inquiry.dto.InquiryDto;
import com.e1i6.notionable.domain.inquiryanswer.inquiry.service.InquiryService;
import com.e1i6.notionable.domain.user.data.dto.UserDto;
import com.e1i6.notionable.global.auth.JwtProvider;
import com.e1i6.notionable.global.auth.JwtUtil;
import com.e1i6.notionable.global.common.response.BaseResponse;
import com.e1i6.notionable.global.common.response.ResponseCode;
import com.e1i6.notionable.global.common.response.ResponseException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class InquiryController {

    private final InquiryService inquiryService;
    private final JwtProvider jwtProvider;
    private final JwtUtil jwtUtil;

    // 유저의 문의 작성
    @PostMapping("/inquiry")
    public BaseResponse<InquiryDto> writeInquiry(@RequestHeader("Authorization") String authorizationHeader,
                                                 @RequestPart InquiryDto inquiryDto,
                                                 @RequestPart MultipartFile file){
        try {
            // 헤더에서 JWT 토큰 추출
            String accessToken = authorizationHeader.replace("Bearer ", "");
            UserDto userDto = null;

            // 토큰 검증
            if (jwtProvider.validateToken(accessToken))
                userDto = jwtUtil.getUserFromToken(accessToken);

            // 문의 작성 후 작성 내용 반환
            InquiryDto inquiryInformation = inquiryService.writeInquiry(userDto.getUserId(), inquiryDto, file);

            return new BaseResponse<>(inquiryInformation);
        } catch (ResponseException e) {
            return new BaseResponse<>(e.getErrorCode(), e.getMessage());
        } catch (Exception e) {
            return new BaseResponse<>(ResponseCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    // 마이페이지(유저) - 유저 자신의 모든 문의 및 답변 조회
    @GetMapping("/inquiry-answer")
    public BaseResponse<List<InquiryAnswerDto>> getAllInquiryAnswer(@RequestHeader("Authorization") String authorizationHeader){
        try {
            // 헤더에서 JWT 토큰 추출
            String accessToken = authorizationHeader.replace("Bearer ", "");
            UserDto userDto = null;

            // 토큰 검증
            if (jwtProvider.validateToken(accessToken))
                userDto = jwtUtil.getUserFromToken(accessToken);

            // 유저 자신의 모든 문의 내역 반환
            List<InquiryAnswerDto> inquiryAnswerDtoList = inquiryService.getAllInquiryAnswer(userDto.getUserId());

            return new BaseResponse<>(inquiryAnswerDtoList);
        } catch (ResponseException e) {
            return new BaseResponse<>(e.getErrorCode(), e.getMessage());
        } catch (Exception e) {
            return new BaseResponse<>(ResponseCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
