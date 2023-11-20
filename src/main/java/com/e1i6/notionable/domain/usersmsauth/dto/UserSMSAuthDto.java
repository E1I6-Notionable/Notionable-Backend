package com.e1i6.notionable.domain.usersmsauth.dto;

import com.e1i6.notionable.domain.usersmsauth.entity.UserSMSAuthEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserSMSAuthDto {

    private long id;
    private String phoneNumber;
    private String authCode;

    public static UserSMSAuthDto toUserSMSDto(UserSMSAuthEntity userSMSAuthEntity) {
        UserSMSAuthDto userSMSAuthDto = new UserSMSAuthDto();
        userSMSAuthDto.setId(userSMSAuthEntity.getId());
        userSMSAuthDto.setPhoneNumber(userSMSAuthEntity.getPhoneNumber());
        userSMSAuthDto.setAuthCode(userSMSAuthEntity.getAuthCode());

        return userSMSAuthDto;
    }
}
