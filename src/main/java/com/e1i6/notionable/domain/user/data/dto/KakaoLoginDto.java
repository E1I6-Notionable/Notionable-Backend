package com.e1i6.notionable.domain.user.data.dto;

import com.e1i6.notionable.global.auth.JwtDto;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
public class KakaoLoginDto {
    private JwtDto jwtDto;
    private String email;
    private String nickName;
    private String password;
    private String profile;
    private String phoneNumber;

}
