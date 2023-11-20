package com.e1i6.notionable.coolsms_auth.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
public class CoolSMSDto {

    private int id;
    private String phoneNumber;
    private String authCode;

}
