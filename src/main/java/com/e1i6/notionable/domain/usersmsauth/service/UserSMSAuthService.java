package com.e1i6.notionable.domain.usersmsauth.service;

import com.e1i6.notionable.domain.usersmsauth.dto.UserSMSAuthDto;
import com.e1i6.notionable.domain.usersmsauth.entity.UserSMSAuthEntity;
import com.e1i6.notionable.domain.usersmsauth.repository.UserSMSAuthRepository;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class UserSMSAuthService {

    @Autowired
    private UserSMSAuthRepository userSMSAuthRepository;

    private DefaultMessageService messageService;
    private Environment env;

    // 생성자를 통해 Environment 객체 주입
    @Autowired
    public UserSMSAuthService(Environment env) {
        this.env = env;
    }

    public UserSMSAuthDto sendAuthCode(UserSMSAuthDto userSMSAuthDto) {
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
        message.setTo(userSMSAuthDto.getPhoneNumber());
        message.setText("notionable 회원가입 인증번호는 [" + authCode + "]입니다.");

        userSMSAuthDto.setAuthCode(authCode);

        messageService.sendOne(new SingleMessageSendingRequest(message));

        UserSMSAuthEntity userSMSAuthEntity = userSMSAuthRepository.findByPhoneNumber(userSMSAuthDto.getPhoneNumber());
        UserSMSAuthDto resultUserSMSAuthDto;
        if(userSMSAuthEntity == null){
            resultUserSMSAuthDto = userSMSAuthDto;
            userSMSAuthRepository.save(UserSMSAuthEntity.toUserSMSEntity(userSMSAuthDto));
        }
        else{
            userSMSAuthEntity.setAuthCode(userSMSAuthDto.getAuthCode());
            resultUserSMSAuthDto = UserSMSAuthDto.toUserSMSDto(userSMSAuthEntity);
            userSMSAuthRepository.save(userSMSAuthEntity);
        }

        return resultUserSMSAuthDto;
    }
}