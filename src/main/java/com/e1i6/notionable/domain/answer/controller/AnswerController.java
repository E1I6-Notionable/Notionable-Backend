package com.e1i6.notionable.domain.answer.controller;

import com.e1i6.notionable.domain.answer.dto.AnswerDto;
import com.e1i6.notionable.domain.answer.service.AnswerService;
import com.e1i6.notionable.domain.inquiry.dto.InquiryDto;
import com.e1i6.notionable.domain.inquiry.service.InquiryService;
import com.e1i6.notionable.domain.user.data.dto.UserDto;
import com.e1i6.notionable.global.auth.JwtProvider;
import com.e1i6.notionable.global.auth.JwtUtil;
import com.e1i6.notionable.global.common.response.BaseResponse;
import com.e1i6.notionable.global.common.response.ResponseCode;
import com.e1i6.notionable.global.common.response.ResponseException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
}
