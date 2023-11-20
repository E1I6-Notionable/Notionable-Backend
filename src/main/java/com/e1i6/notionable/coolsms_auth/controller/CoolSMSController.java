package com.e1i6.notionable.coolsms_auth.controller;

import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RestController;

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
}