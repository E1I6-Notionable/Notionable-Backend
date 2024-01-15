package com.e1i6.notionable.domain.inquiryanswer.answer.controller;

import com.e1i6.notionable.domain.inquiryanswer.InquiryAnswerDto;
import com.e1i6.notionable.domain.inquiryanswer.answer.dto.AnswerDto;
import com.e1i6.notionable.domain.inquiryanswer.answer.service.AnswerService;
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
@RequestMapping("/creator")
public class AnswerController {

    private final AnswerService answerService;
    private final JwtProvider jwtProvider;
    private final JwtUtil jwtUtil;

    // 크리에이터의 답변 작성
    @PostMapping("/answer")
    public BaseResponse<AnswerDto> writeAnswer(@RequestHeader("Authorization") String authorizationHeader,
                                               @RequestPart AnswerDto answerDto,
                                               @RequestPart MultipartFile file){
        try {
            // 헤더에서 JWT 토큰 추출
            String accessToken = authorizationHeader.replace("Bearer ", "");
            UserDto userDto = null;

            // 토큰 검증
            if (jwtProvider.validateToken(accessToken))
                userDto = jwtUtil.getUserFromToken(accessToken);

            // 답변 작성 후 작성 내용 반환
            AnswerDto answerInformation = answerService.writeAnswer(userDto.getUserId(), answerDto, file);

            return new BaseResponse<>(answerInformation);
        } catch (ResponseException e) {
            return new BaseResponse<>(e.getErrorCode(), e.getMessage());
        } catch (Exception e) {
            return new BaseResponse<>(ResponseCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    // 마이페이지(크리에이터) - 크리에이터 자신에게 온 모든 문의와 답변 조회
    @GetMapping("/inquiry-answer")
    public BaseResponse<List<InquiryAnswerDto>> getAllInquiryAnswer(@RequestHeader("Authorization") String authorizationHeader){
        try {
            // 헤더에서 JWT 토큰 추출
            String accessToken = authorizationHeader.replace("Bearer ", "");
            UserDto userDto = null;

            // 토큰 검증
            if (jwtProvider.validateToken(accessToken))
                userDto = jwtUtil.getUserFromToken(accessToken);

            // 크리에이터 자신에게 온 모든 문의와 답변 내역 반환
            List<InquiryAnswerDto> inquiryAnswerDtoList = answerService.getAllInquiryAnswer(userDto.getUserId());

            return new BaseResponse<>(inquiryAnswerDtoList);
        } catch (ResponseException e) {
            return new BaseResponse<>(e.getErrorCode(), e.getMessage());
        } catch (Exception e) {
            return new BaseResponse<>(ResponseCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
