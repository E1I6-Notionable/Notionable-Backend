package com.e1i6.notionable.domain.template.controller;

import com.e1i6.notionable.domain.template.data.dto.FreeTemplateDto;
import com.e1i6.notionable.domain.template.data.dto.UploadFreeTemplateReqDto;
import com.e1i6.notionable.domain.template.service.FreeTemplateService;
import com.e1i6.notionable.global.common.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
    public BaseResponse<List<FreeTemplateDto>> getRecommendedFreeTemplate() {
        List<FreeTemplateDto> recommendedFreeTemplates = freeTemplateService.findRecommendTemplate();
        return new BaseResponse<>(recommendedFreeTemplates);
    }

    @GetMapping("")
    public BaseResponse<List<FreeTemplateDto>> getFreeTemplateListWithFilter (
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "category", required = false, defaultValue = "all") String category,
            @RequestParam(value = "criteria", required = false, defaultValue = "createdAt") String criteria) {

        return new BaseResponse<>(freeTemplateService.getFreeTemplatesWithCriteria(page, category, criteria));
    }
}
