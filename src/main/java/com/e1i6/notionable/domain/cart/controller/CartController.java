package com.e1i6.notionable.domain.cart.controller;

import com.e1i6.notionable.domain.cart.dto.CartDto;
import com.e1i6.notionable.domain.cart.service.CartService;
import com.e1i6.notionable.domain.user.data.dto.UserDto;
import com.e1i6.notionable.global.auth.JwtProvider;
import com.e1i6.notionable.global.auth.JwtUtil;
import com.e1i6.notionable.global.common.response.BaseResponse;
import com.e1i6.notionable.global.common.response.ResponseCode;
import com.e1i6.notionable.global.common.response.ResponseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@Slf4j
public class CartController {

    private final CartService cartService;
    private final JwtProvider jwtProvider;
    private final JwtUtil jwtUtil;

    @GetMapping("/my-cart")
    public BaseResponse<List<CartDto>> getMyCartInformation(@RequestHeader("Authorization") String authorizationHeader) {

        try {
            // 헤더에서 JWT 토큰 추출
            String accessToken = authorizationHeader.replace("Bearer ", "");
            UserDto userDto = null;

            // 토큰 검증
            if (jwtProvider.validateToken(accessToken))
                userDto = jwtUtil.getUserFromToken(accessToken);

            // 사용자 정보를 기반으로 장바구니 정보 가져오기
            List<CartDto> cartInformation = cartService.getMyCartInformation(userDto.getUserId());

            return new BaseResponse<>(cartInformation);
        } catch (ResponseException e) {
            return new BaseResponse<>(e.getErrorCode(), e.getMessage());
        } catch (Exception e) {
            return new BaseResponse<>(ResponseCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PostMapping("/my-cart/add")
    public BaseResponse<?> addMyCartInformation(@RequestHeader("Authorization") String authorizationHeader
    , @RequestBody CartDto cartDto){

        try {
            // 헤더에서 JWT 토큰 추출
            String accessToken = authorizationHeader.replace("Bearer ", "");
            UserDto userDto = null;

            // 토큰 검증
            if (jwtProvider.validateToken(accessToken))
                userDto = jwtUtil.getUserFromToken(accessToken);

            // 장바구니 추가 로직
            CartDto cartInformation = cartService.addMyCartInformation(userDto.getUserId(), cartDto);

            // 장바구니에 추가된 데이터 반환
            return new BaseResponse<>(cartInformation);
        } catch (ResponseException e) {
            return new BaseResponse<>(e.getErrorCode(), e.getMessage());
        } catch (Exception e) {
            return new BaseResponse<>(ResponseCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @DeleteMapping("/my-cart/delete")
    public BaseResponse<> deleteMyCartInformation(@RequestHeader("Authorization") String authorizationHeader, @RequestBody CartDto cartDto){

        String accessToken = authorizationHeader.replace("Bearer ", "");

        // 토큰 검증
        if (jwtProvider.validateToken(accessToken)) {

            // 토큰이 유효한 경우, 사용자 정보 얻기
            UserDto userDto = jwtUtil.getUserFromToken(accessToken);

            // 장바구니 삭제 로직
            CartDto cartInformation = cartService.deleteMyCartInformation(userDto.getUserId(), cartDto.getItemId());

            // 장바구니에 추가된 데이터 반환
            return new BaseResponse<>(cartInformation);
        } else {
            // 토큰이 유효하지 않은 경우에 대한 처리
            return new BaseResponse<>(ResponseCode.NOT_FOUND, "Not valid token");
        }
    }

}
