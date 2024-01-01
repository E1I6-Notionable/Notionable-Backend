package com.e1i6.notionable.domain.template.controller;

import com.e1i6.notionable.domain.template.data.dto.TemplateDetailDto;
import com.e1i6.notionable.domain.template.data.dto.TemplateDto;
import com.e1i6.notionable.domain.template.data.dto.UploadTemplateReqDto;
import com.e1i6.notionable.domain.template.service.TemplateService;
import com.e1i6.notionable.global.common.response.BaseResponse;
import com.e1i6.notionable.global.common.response.ResponseCode;
import com.e1i6.notionable.global.common.response.ResponseException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/template")
@RequiredArgsConstructor
public class TemplateController {

    private final TemplateService templateService;

    @PostMapping("")
    public BaseResponse<String> uploadTemplate(
            @RequestPart UploadTemplateReqDto reqDto,
            @RequestPart("files") List<MultipartFile> multipartFiles) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(authentication.getName());

        String message = templateService.uploadTemplate(userId, multipartFiles, reqDto);
        return new BaseResponse<>(message);
    }

    @GetMapping("/recommend-free")
    public BaseResponse<List<TemplateDto>> getRecommendedFreeTemplate() {
        List<TemplateDto> recommendedTemplates = templateService.getRecommendFreeTemplates();
        return new BaseResponse<>(recommendedTemplates);
    }

    @GetMapping("/recommend-paid")
    public BaseResponse<List<TemplateDto>> getRecommendedPaidTemplate() {
        List<TemplateDto> recommendedTemplates = templateService.getRecommendPaidTemplates();
        return new BaseResponse<>(recommendedTemplates);
    }
/*

    @GetMapping("/free")
    public BaseResponse<List<TemplateDto>> getFreeTemplateListWithFilter (
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "category", required = false, defaultValue = "all") String category,
            @RequestParam(value = "criteria", required = false, defaultValue = "createdAt") String criteria) {

        return new BaseResponse<>(templateService.getFreeTemplatesWithCriteria(page, category, criteria));
    }

    @GetMapping("/paid")
    public BaseResponse<List<TemplateDto>> getPaidTemplateListWithFilter (
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "category", required = false, defaultValue = "all") String category,
            @RequestParam(value = "criteria", required = false, defaultValue = "createdAt") String criteria,
            @RequestParam(value = "criteria-option", required = false, defaultValue = "DESC") String criteriaOption) {

        return new BaseResponse<>(templateService.getPaidTemplatesWithCriteria(page, category, criteria, criteriaOption));
    }
*/

    @GetMapping("/filter")
    public BaseResponse<List<TemplateDto>> getTemplateListWithFilter (
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
            @RequestParam(value = "template-type", required = false, defaultValue = "") String templateType,
            @RequestParam(value = "category", required = false, defaultValue = "") String category,
            @RequestParam(value = "criteria", required = false, defaultValue = "createdAt") String criteria,
            @RequestParam(value = "criteria-option", required = false, defaultValue = "desc") String criteriaOption) {

        return new BaseResponse<>(templateService.getTemplatesWithFilter(
                page, keyword, templateType, category, criteria, criteriaOption));
    }

    @GetMapping("/detail/{templateId}")
    public BaseResponse<TemplateDetailDto> getTemplateDetail(@PathVariable Long templateId) {
        try {
            TemplateDetailDto resDto = templateService.getTemplateDetail(templateId);
            return new BaseResponse<>(resDto);
        } catch (ResponseException e) {
            return new BaseResponse<>(e.getErrorCode(), e.getMessage());
        } catch (Exception e) {
            return new BaseResponse<>(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/search/")

    @PostMapping("/delete/{templateId}")
    public BaseResponse<String> deleteTemplate(@PathVariable Long templateId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(authentication.getName());

        try {
            return new BaseResponse<>(templateService.deleteTemplate(userId, templateId));
        } catch (ResponseException e) {
            return new BaseResponse<>(e.getErrorCode(), e.getMessage());
        } catch (Exception e) {
            return new BaseResponse<>(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }
}
