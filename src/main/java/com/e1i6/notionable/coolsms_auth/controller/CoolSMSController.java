package com.e1i6.notionable.coolsms_auth.controller;

import com.e1i6.notionable.coolsms_auth.dto.CoolSMSDto;
import com.e1i6.notionable.coolsms_auth.service.CoolSMSService;
import lombok.RequiredArgsConstructor;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CoolSMSController {

    @Autowired
    private CoolSMSService coolSMSService;

    @PostMapping("/users/signup/send-code")
    public String sendAuthCode(@RequestBody CoolSMSDto coolSMSDto) {

        SingleMessageSentResponse response = coolSMSService.sendAuthCode(coolSMSDto);

        if(response == null)
            return "failed!";
        return "success!";
    }
}