package com.e1i6.notionable.coolsms_auth.controller;

import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CoolSMSController {

    private final DefaultMessageService messageService;

    public CoolSMSController () {
        this.messageService = NurigoApp.INSTANCE.initialize("발급받은 API key",
                "발급받은 SECRET key", "https://api.coolsms.co.kr");
    }
}