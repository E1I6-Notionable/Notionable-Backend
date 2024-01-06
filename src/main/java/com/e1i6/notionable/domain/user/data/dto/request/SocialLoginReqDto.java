package com.e1i6.notionable.domain.user.data.dto.request;

import lombok.Data;

@Data
public class SocialLoginReqDto {
    private String code;
    private String socialType;
}
