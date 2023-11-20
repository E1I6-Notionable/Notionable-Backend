package com.e1i6.notionable.coolsms_auth.controller;

import com.e1i6.notionable.coolsms_auth.dto.CoolSMSDto;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
public class CoolSMSController {

    private final DefaultMessageService messageService;
    private final Environment env;

    public CoolSMSController (Environment env) {
        String api_key = env.getProperty("cool.sms.api.key");
        String api_secret = env.getProperty("cool.sms.api.secret");
        this.env = env;
        this.messageService = NurigoApp.INSTANCE.initialize(api_key,
                api_secret, "https://api.coolsms.co.kr");
    }

    @PostMapping("/users/signup/send-code")
    public SingleMessageSentResponse sendAuthCode(@RequestBody CoolSMSDto coolSMSDto) {
        Random random = new Random();
        Message message = new Message();
        String sendNumber = env.getProperty("cool.sms.phone.number");
        String authCode = String.valueOf(random.nextInt(8888)+1000); // 범위 : 1000 ~ 9999

        message.setFrom(sendNumber);
        message.setTo(coolSMSDto.getPhoneNumber());
        message.setText("notionable 회원가입 인증번호는 [" + authCode + "]입니다.");

        SingleMessageSentResponse response = this.messageService.sendOne(new SingleMessageSendingRequest(message));
        System.out.println(response);

        return response;
    }
}