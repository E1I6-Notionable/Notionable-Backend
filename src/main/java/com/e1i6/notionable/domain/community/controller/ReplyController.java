package com.e1i6.notionable.domain.community.controller;

import com.e1i6.notionable.domain.community.dto.community.ReplyReq;
import com.e1i6.notionable.domain.community.service.ReplyService;
import com.e1i6.notionable.domain.user.data.dto.UserDto;
import com.e1i6.notionable.global.auth.JwtProvider;
import com.e1i6.notionable.global.auth.JwtUtil;
import com.e1i6.notionable.global.common.response.BaseResponse;
import com.e1i6.notionable.global.common.response.ResponseCode;
import com.e1i6.notionable.global.common.response.ResponseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/reply")
@Slf4j
public class ReplyController {
    private final ReplyService replyService;
    private final JwtProvider jwtProvider;
    private final JwtUtil jwtUtil;

    //댓글 작성
    @PostMapping("/{commentId}")
    public BaseResponse<Long> addReply(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable Long commentId,
            @RequestBody ReplyReq replyReq){
        try {
            // 헤더에서 JWT 토큰 추출
            String accessToken = authorizationHeader.replace("Bearer ", "");
            UserDto userDto = null;

            // 토큰 검증
            if (jwtProvider.validateToken(accessToken))
                userDto = jwtUtil.getUserFromToken(accessToken);

            // 추가한 대댓글 id 반환
            return new BaseResponse<>(replyService.addReply(userDto.getUserId(), commentId,replyReq));
        } catch (ResponseException e) {
            return new BaseResponse<>(e.getErrorCode(), e.getMessage());
        } catch (Exception e) {
            return new BaseResponse<>(ResponseCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    //대댓글 전체 댓글 조회
    @GetMapping("/{commentId}")
    public BaseResponse<?> getReply(@PathVariable Long commentId,
                                        @PageableDefault(size = 6, sort = "createdAt",
                                                direction = Sort.Direction.DESC)
                                        Pageable pageable) {
        try {
            return new BaseResponse<>(replyService.getAllReply(commentId, pageable));
        } catch (ResponseException e) {
            return new BaseResponse<>(e.getErrorCode(), e.getMessage());
        }    catch (Exception e) {
            return new BaseResponse<>(ResponseCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
