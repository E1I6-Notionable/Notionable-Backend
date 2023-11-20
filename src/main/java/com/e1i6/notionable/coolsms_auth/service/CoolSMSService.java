package com.e1i6.notionable.coolsms_auth.service;

import com.e1i6.notionable.coolsms_auth.dto.CoolSMSDto;
import com.e1i6.notionable.coolsms_auth.entity.CoolSMSEntity;
import com.e1i6.notionable.coolsms_auth.repository.CoolSMSRepository;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class CoolSMSService {

    @Autowired
    private CoolSMSRepository coolSMSRepository;

    private DefaultMessageService messageService;
    private Environment env;

    // 생성자를 통해 Environment 객체 주입
    @Autowired
    public CoolSMSService(Environment env) {
        this.env = env;
    }

    public SingleMessageSentResponse sendAuthCode(CoolSMSDto coolSMSDto) {
        // Environment 객체를 사용하여 API 키와 비밀번호 가져오기
        String api_key = env.getProperty("cool.sms.api.key");
        String api_secret = env.getProperty("cool.sms.api.secret");
        this.messageService = NurigoApp.INSTANCE.initialize(api_key,
                api_secret, "https://api.coolsms.co.kr");

        Random random = new Random();
        String sendNumber = env.getProperty("cool.sms.phone.number");
        String authCode = String.valueOf(random.nextInt(8888) + 1000); // 범위: 1000 ~ 9999

        Message message = new Message();
        message.setFrom(sendNumber);
        message.setTo(coolSMSDto.getPhoneNumber());
        message.setText("notionable 회원가입 인증번호는 [" + authCode + "]입니다.");

        coolSMSDto.setAuthCode(authCode);

        if(coolSMSRepository.findByPhoneNumber(coolSMSDto.getPhoneNumber())==null)
            coolSMSRepository.save(CoolSMSEntity.toCoolSMSEntity(coolSMSDto));
        else
            return null;
        return messageService.sendOne(new SingleMessageSendingRequest(message));
    }
}