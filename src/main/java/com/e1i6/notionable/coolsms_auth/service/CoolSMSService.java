package com.e1i6.notionable.coolsms_auth.service;

import com.e1i6.notionable.coolsms_auth.dto.CoolSMSDto;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class CoolSMSService {

    private final DefaultMessageService messageService;
    private Environment env;

    public CoolSMSService(Environment env) {
        String api_key = env.getProperty("cool.sms.api.key");
        String api_secret = env.getProperty("cool.sms.api.secret");
        this.messageService = NurigoApp.INSTANCE.initialize(api_key,
                api_secret, "https://api.coolsms.co.kr");
        this.env = env;
    }

    public SingleMessageSentResponse sendAuthCode(CoolSMSDto coolSMSDto) {

        Random random = new Random();
        String sendNumber = env.getProperty("cool.sms.phone.number");
        String authCode = String.valueOf(random.nextInt(8888)+1000); // 범위 : 1000 ~ 9999

        Message message = new Message();
        message.setFrom(sendNumber);
        message.setTo(coolSMSDto.getPhoneNumber());
        message.setText("notionable 회원가입 인증번호는 [" + authCode + "]입니다.");


        SingleMessageSentResponse response = messageService.sendOne(new SingleMessageSendingRequest(message));

        return response;
    }
}
