package com.e1i6.notionable.global.auth;

import com.e1i6.notionable.domain.user.data.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtUtil {

    private final JwtProvider jwtProvider;

    public UserDto getUserFromToken(String accessToken) {
        // 토큰을 사용하여 Authentication 객체 생성
        Authentication authentication = jwtProvider.getAuthentication(accessToken);

        // Authentication 객체에서 UserDetails 추출
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        // UserDetails에서 UserDto로 변환
        UserDto userDto = new UserDto();

        userDto.setUserId(Long.parseLong(userDetails.getUsername()));

        return userDto;
    }
}
