package com.e1i6.notionable.domain.cart.controller;

import com.e1i6.notionable.domain.cart.dto.CartDto;
import com.e1i6.notionable.domain.cart.service.CartService;
import com.e1i6.notionable.domain.user.data.dto.UserDto;
import com.e1i6.notionable.global.auth.JwtProvider;
import com.e1i6.notionable.global.auth.JwtUtil;
import com.e1i6.notionable.global.common.response.BaseResponse;
import com.e1i6.notionable.global.common.response.ResponseCode;
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
        // 헤더에서 JWT 토큰 추출
        String accessToken = authorizationHeader.replace("Bearer ", "");

        // 토큰 검증
        if (jwtProvider.validateToken(accessToken)) {
            // 토큰이 유효한 경우, 사용자 정보 얻기
            UserDto userDto = jwtUtil.getUserFromToken(accessToken);

            // 사용자 정보를 기반으로 장바구니 정보 가져오기
            List<CartDto> cartInformation = cartService.getMyCartInformation(userDto.getUserId());
            log.info("cart = {}", cartInformation);
            return new BaseResponse<>(cartInformation);
        } else {
            // 토큰이 유효하지 않은 경우에 대한 처리
            return new BaseResponse<>(ResponseCode.NOT_FOUND, "Not valid token");
        }
    }

    @PostMapping("/my-cart/add")
    public BaseResponse<?> addMyCartInformation(@RequestHeader("Authorization") String authorizationHeader
    , @RequestBody CartDto cartDto){
        String accessToken = authorizationHeader.replace("Bearer ", "");

        // 토큰 검증
        if (jwtProvider.validateToken(accessToken)) {
            // 토큰이 유효한 경우, 사용자 정보 얻기
            UserDto userDto = jwtUtil.getUserFromToken(accessToken);

            // 사용자 정보를 기반으로 장바구니 정보 가져오기
            CartDto cartInformation = cartService.addMyCartInformation(userDto.getUserId(), cartDto);

            return new BaseResponse<>(cartInformation);
        } else {
            // 토큰이 유효하지 않은 경우에 대한 처리
            return new BaseResponse<>(ResponseCode.NOT_FOUND, "Not valid token");
        }

    }

}
