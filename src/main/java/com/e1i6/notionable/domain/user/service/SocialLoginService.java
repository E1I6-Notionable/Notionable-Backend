package com.e1i6.notionable.domain.user.service;

import com.e1i6.notionable.domain.user.data.dto.response.SocialLoginResDto;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface SocialLoginService {
    SocialLoginResDto socialLogin(String code, String type) throws JsonProcessingException;
}
