package com.e1i6.notionable.domain.community.controller;

import com.e1i6.notionable.domain.community.dto.CommentReq;
import com.e1i6.notionable.domain.community.service.CommentService;
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
@RequestMapping(value = "/comments")
@Slf4j
public class CommentController {
    private final CommentService commentService;
    private final JwtProvider jwtProvider;
    private final JwtUtil jwtUtil;

    //댓글 작성
    @PostMapping("/{communityId}")
    public BaseResponse<Long> addComment(@RequestHeader("Authorization") String authorizationHeader,
            @PathVariable Long communityId,
            @RequestBody CommentReq commentReq){
        try {
            // 추가한 댓글 id 반환
            return new BaseResponse<>(commentService.addComment(getUserIdFromToken(authorizationHeader), communityId,commentReq));
        } catch (ResponseException e) {
            return new BaseResponse<>(e.getErrorCode(), e.getMessage());
        } catch (Exception e) {
            return new BaseResponse<>(ResponseCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    //게시글 전체 댓글 조회
    @GetMapping("/{communityId}")
    public BaseResponse<?> getComment(@PathVariable Long communityId,
                                        @PageableDefault(size = 6, sort = "createdAt",
                                                direction = Sort.Direction.DESC)
                                        Pageable pageable) {
        try {
            return new BaseResponse<>(commentService.getAllComment(communityId, pageable));
        } catch (ResponseException e) {
            return new BaseResponse<>(e.getErrorCode(), e.getMessage());
        }    catch (Exception e) {
            return new BaseResponse<>(ResponseCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    //대댓글 삭제
    @DeleteMapping("/{commentId}")
    public BaseResponse<String> deleteReply(@RequestHeader("Authorization") String authorizationHeader,
                                            @PathVariable Long commentId){
        return new BaseResponse<>(commentService.deleteComment(getUserIdFromToken(authorizationHeader), commentId));
    }

    //댓글 수정
    @PutMapping("/{commentId}")
    public BaseResponse<String> modifyComment(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable Long commentId,
            @RequestBody CommentReq commentReq){
        try {
            return new BaseResponse<>(commentService.modifyComment(getUserIdFromToken(authorizationHeader), commentId,commentReq));
        } catch (ResponseException e) {
            return new BaseResponse<>(e.getErrorCode(), e.getMessage());
        } catch (Exception e) {
            return new BaseResponse<>(ResponseCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }


    //헤더에서 토큰 추출 & 검증
    public Long getUserIdFromToken(String authorizationHeader) {
        if(authorizationHeader != null){
            String accessToken = authorizationHeader.replace("Bearer ", "");
            UserDto userDto = null;

            if (jwtProvider.validateToken(accessToken)){
                userDto = jwtUtil.getUserFromToken(accessToken);
                return userDto.getUserId();}
        }
        return null;
    }

}
