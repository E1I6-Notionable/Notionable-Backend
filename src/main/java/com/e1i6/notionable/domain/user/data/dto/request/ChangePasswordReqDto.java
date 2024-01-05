package com.e1i6.notionable.domain.user.data.dto.request;

import lombok.Data;

@Data
public class ChangePasswordReqDto {
    private String originPassword;
    private String newPassword;
}
