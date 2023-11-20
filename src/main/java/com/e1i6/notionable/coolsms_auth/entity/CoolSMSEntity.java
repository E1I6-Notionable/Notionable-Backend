package com.e1i6.notionable.coolsms_auth.entity;

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
    private int id;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String authCode;
}
