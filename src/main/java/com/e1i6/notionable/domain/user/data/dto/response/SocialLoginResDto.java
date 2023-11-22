package com.e1i6.notionable.domain.user.data.dto.response;

import com.e1i6.notionable.global.auth.JwtDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class SocialLoginResDto {
    private JwtDto jwtDto;
    private String email;
    private String nickName;
    private String profile;
    private String phoneNumber;
}
