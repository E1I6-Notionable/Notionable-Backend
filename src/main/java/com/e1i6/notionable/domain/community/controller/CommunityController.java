package com.e1i6.notionable.domain.community.controller;

import com.e1i6.notionable.domain.community.dto.CommunityReq;
import com.e1i6.notionable.domain.community.dto.CommunityRes;
import com.e1i6.notionable.domain.community.service.CommunityServiceImpl;
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

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/community")
@Slf4j
public class CommunityController {
    private final CommunityServiceImpl communityService;
    private final JwtProvider jwtProvider;
    private final JwtUtil jwtUtil;

//    게시글 조회
    @GetMapping("")
    public BaseResponse<?> getAllCommunity(@RequestParam(required = false) String keyword,
                                                            @RequestParam(required = false) String filter,
                                                         @PageableDefault(size = 5, sort = "createdAt",
                                                                    direction = Sort.Direction.DESC)
                                                            Pageable pageable) {
        try {
            List<CommunityRes> communityList = communityService.getAllCommunity(keyword, filter, pageable);
            return new BaseResponse<>(communityList);
        } catch (ResponseException e) {
            return new BaseResponse<>(e.getErrorCode(), e.getMessage());
        }    catch (Exception e) {
            return new BaseResponse<>(ResponseCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }


    @PostMapping("/add")
    public BaseResponse<?> addMyCommunityInformation(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody CommunityReq communityReq){
        try {
            // 헤더에서 JWT 토큰 추출
            String accessToken = authorizationHeader.replace("Bearer ", "");
            UserDto userDto = null;

            // 토큰 검증
            if (jwtProvider.validateToken(accessToken))
                userDto = jwtUtil.getUserFromToken(accessToken);

            // 커뮤니티 글쓰기
            CommunityReq communityInformation = communityService.addCommunityInformation(userDto.getUserId(), communityReq);

            // 커뮤니티에 추가된 데이터 반환
            return new BaseResponse<>(communityInformation);
        } catch (ResponseException e) {
            return new BaseResponse<>(e.getErrorCode(), e.getMessage());
        } catch (Exception e) {
            return new BaseResponse<>(ResponseCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }


}
