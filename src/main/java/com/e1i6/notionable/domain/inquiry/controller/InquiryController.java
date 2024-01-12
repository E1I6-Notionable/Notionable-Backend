package com.e1i6.notionable.domain.inquiry.controller;

import com.e1i6.notionable.domain.inquiry.service.InquiryService;
import com.e1i6.notionable.global.auth.JwtProvider;
import com.e1i6.notionable.global.auth.JwtUtil;
import com.e1i6.notionable.global.common.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class InquiryController {

    private final InquiryService inquiryService;
    private final JwtProvider jwtProvider;
    private final JwtUtil jwtUtil;

    @PostMapping("/inquiry")
    public BaseResponse<>

}
