package com.e1i6.notionable.domain.community.controller;

import com.e1i6.notionable.domain.community.dto.community.CommunityReq;
import com.e1i6.notionable.domain.community.service.CommunityService;
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
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/posts")
@Slf4j
public class CommunityController {
    private final CommunityService communityService;
    private final JwtProvider jwtProvider;
    private final JwtUtil jwtUtil;

//    게시글 목록 조회
    @GetMapping("/all")
    public BaseResponse<?> getCommunity(@RequestParam(required = false) String keyword,
                                                            @RequestParam(required = false) String filter,
                                                         @PageableDefault(size = 6, sort = "createdAt",
                                                                    direction = Sort.Direction.DESC)
                                                            Pageable pageable) {
        try {
            return new BaseResponse<>(communityService.getCommunity(keyword, filter, pageable));
        } catch (ResponseException e) {
            return new BaseResponse<>(e.getErrorCode(), e.getMessage());
        }    catch (Exception e) {
            return new BaseResponse<>(ResponseCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    // 게시글 작성
    @PostMapping("/add")
    public BaseResponse<Long> addCommunity(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestPart CommunityReq communityReq,
            @RequestPart("files") List<MultipartFile> multipartFiles){
        try {
            // 헤더에서 JWT 토큰 추출
            String accessToken = authorizationHeader.replace("Bearer ", "");
            UserDto userDto = null;

            // 토큰 검증
            if (jwtProvider.validateToken(accessToken))
                userDto = jwtUtil.getUserFromToken(accessToken);

            // 추가한 커뮤니티 id 반환
            return new BaseResponse<>(communityService.addCommunity(userDto.getUserId(), multipartFiles,communityReq));
        } catch (ResponseException e) {
            return new BaseResponse<>(e.getErrorCode(), e.getMessage());
        } catch (Exception e) {
            return new BaseResponse<>(ResponseCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    // 게시글 상세조회
    @GetMapping("/{communityId}")
    public BaseResponse<?> getCommunityDetail(@PathVariable Long communityId)
    {
        return new BaseResponse<>(communityService.getCommunityDetail(communityId));
    }


}
