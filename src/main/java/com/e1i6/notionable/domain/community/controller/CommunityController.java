package com.e1i6.notionable.domain.community.controller;

import com.e1i6.notionable.domain.cart.dto.CartDto;
import com.e1i6.notionable.domain.community.dto.CommunityReq;
import com.e1i6.notionable.domain.community.dto.CommunityRes;
import com.e1i6.notionable.domain.community.entity.Community;
import com.e1i6.notionable.domain.community.service.CommunityService;
import com.e1i6.notionable.domain.user.data.dto.UserDto;
import com.e1i6.notionable.global.auth.JwtProvider;
import com.e1i6.notionable.global.auth.JwtUtil;
import com.e1i6.notionable.global.common.response.BaseResponse;
import com.e1i6.notionable.global.common.response.ResponseCode;
import com.e1i6.notionable.global.common.response.ResponseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
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
    private final CommunityService communityService;
    private final JwtProvider jwtProvider;
    private final JwtUtil jwtUtil;

    @GetMapping("")
    public BaseResponse<String> getAllCommunity() {
        log.info("여기왔나?");
        return new BaseResponse<>("성공했습니다");
    }

//    전체글
//    @GetMapping("")
//    public BaseResponse<List<CommunityRes>> getAllCommunity(@RequestParam(required = false) String keyword,
//                                                            @RequestParam(required = false) String filter,
//                                                            @PageableDefault(size = 5, sort = "community_id",
//                                                                    direction = Sort.Direction.DESC)
//                                                                Pageable pageable) {
//        log.info("여기왔나?");
//        return new BaseResponse<>(communityService.getAllCommunity(keyword, filter, pageable));
//    }


    @PostMapping("/add")
    public BaseResponse<?> addMyCommunityInformation(@RequestHeader("Authorization") String authorizationHeader
            , @RequestBody CommunityReq communityReq){
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
