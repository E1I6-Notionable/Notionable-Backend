package com.e1i6.notionable.domain.usersmsauth.entity;

import com.e1i6.notionable.domain.usersmsauth.dto.UserSMSAuthDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@Table(name = "user_sms_auth")
public class UserSMSAuthEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String authCode;

    public static UserSMSAuthEntity toUserSMSEntity(UserSMSAuthDto userSMSAuthDto) {
        UserSMSAuthEntity userSMSAuthEntity = new UserSMSAuthEntity();
        userSMSAuthEntity.setId(userSMSAuthDto.getId());
        userSMSAuthEntity.setPhoneNumber(userSMSAuthDto.getPhoneNumber());
        userSMSAuthEntity.setAuthCode(userSMSAuthDto.getAuthCode());

        return userSMSAuthEntity;
    }
}
