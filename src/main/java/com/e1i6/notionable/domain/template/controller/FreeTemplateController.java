package com.e1i6.notionable.domain.template.controller;

import com.e1i6.notionable.domain.template.data.dto.FreeTemplateResDto;
import com.e1i6.notionable.domain.template.data.dto.UploadFreeTemplateReqDto;
import com.e1i6.notionable.domain.template.service.FreeTemplateService;
import com.e1i6.notionable.global.common.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/free-template")
@RequiredArgsConstructor
public class FreeTemplateController {

    private final FreeTemplateService freeTemplateService;

    @PostMapping("")
    public BaseResponse<String> uploadFreeTemplate(
            @RequestPart UploadFreeTemplateReqDto reqDto,
            @RequestPart("files") List<MultipartFile> multipartFiles) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(authentication.getName());

        String message = freeTemplateService.uploadFreeTemplate(userId, multipartFiles, reqDto);
        return new BaseResponse<>(message);
    }

    @GetMapping("/recommend")
    public BaseResponse<List<FreeTemplateResDto>> getRecommendedFreeTemplate() {
        List<FreeTemplateResDto> result = freeTemplateService.findRecommendTemplate();
        return new BaseResponse<>(result);
    }
}
