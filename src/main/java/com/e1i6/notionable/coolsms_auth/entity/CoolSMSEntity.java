package com.e1i6.notionable.coolsms_auth.entity;

import com.e1i6.notionable.coolsms_auth.dto.CoolSMSDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@Table(name = "user_sms_auth")
public class CoolSMSEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String authCode;

    public static CoolSMSEntity toCoolSMSEntity(CoolSMSDto coolSMSDto) {
        CoolSMSEntity coolSMSEntity = new CoolSMSEntity();
        coolSMSEntity.setId(coolSMSDto.getId());
        coolSMSEntity.setPhoneNumber(coolSMSDto.getPhoneNumber());
        coolSMSEntity.setAuthCode(coolSMSDto.getAuthCode());

        return coolSMSEntity;
    }
}
