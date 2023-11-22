package com.e1i6.notionable.domain.user.data.dto.request;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
public class SocialLoginReqDto {
    private String email;
    private String nickName;
    private String password;
    private String profile;
    private String phoneNumber;
    private Integer userType;

}
