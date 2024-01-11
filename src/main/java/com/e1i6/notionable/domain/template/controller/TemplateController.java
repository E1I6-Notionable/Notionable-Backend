package com.e1i6.notionable.domain.template.controller;

import com.e1i6.notionable.domain.template.data.*;
import com.e1i6.notionable.domain.template.service.TemplateService;
import com.e1i6.notionable.global.common.response.BaseResponse;
import com.e1i6.notionable.global.common.response.ResponseCode;
import com.e1i6.notionable.global.common.response.ResponseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/template")
@RequiredArgsConstructor
public class TemplateController {

    private final TemplateService templateService;

    @PostMapping("")
    public BaseResponse<String> uploadTemplate(
            @RequestPart TemplateUploadReqDto reqDto,
            @RequestPart("files") List<MultipartFile> multipartFiles) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(authentication.getName());

        try {
            return new BaseResponse<>(templateService.uploadTemplate(userId, multipartFiles, reqDto));
        } catch (ResponseException e) {
            return new BaseResponse<>(e.getErrorCode(), e.getMessage());
        } catch (Exception e) {
            return new BaseResponse<>(ResponseCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping("/recommend-free")
    public BaseResponse<List<TemplateDto>> getRecommendedFreeTemplate() {
        try {
            return new BaseResponse<>(templateService.getRecommendFreeTemplates());
        } catch (Exception e) {
            return new BaseResponse<>(ResponseCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping("/recommend-paid")
    public BaseResponse<List<TemplateDto>> getRecommendedPaidTemplate() {
        try {
            return new BaseResponse<>(templateService.getRecommendPaidTemplates());
        } catch (Exception e) {
            return new BaseResponse<>(ResponseCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping("/filter")
    public BaseResponse<List<TemplateDto>> getTemplateListWithFilter (
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
            @RequestParam(value = "template-type", required = false, defaultValue = "") String templateType,
            @RequestParam(value = "category", required = false, defaultValue = "") String category,
            @RequestParam(value = "criteria", required = false, defaultValue = "createdAt") String criteria,
            @RequestParam(value = "criteria-option", required = false, defaultValue = "desc") String criteriaOption) {

        try {
            return new BaseResponse<>(templateService.getTemplatesWithFilter(
                    page, keyword, templateType, category, criteria, criteriaOption));
        } catch (ResponseException e) {
            return new BaseResponse<>(e.getErrorCode(), e.getMessage());
        } catch (Exception e) {
            return new BaseResponse<>(ResponseCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping("/detail/{templateId}")
    public BaseResponse<TemplateDetailDto> getTemplateDetail(@PathVariable Long templateId) {
        try {
            TemplateDetailDto resDto = templateService.getTemplateDetail(templateId);
            return new BaseResponse<>(resDto);
        } catch (ResponseException e) {
            return new BaseResponse<>(e.getErrorCode(), e.getMessage());
        } catch (Exception e) {
            return new BaseResponse<>(ResponseCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PutMapping("/{templateId}")
    public BaseResponse<String> updateTemplate(
            @PathVariable Long templateId,
            @RequestPart TemplateUpdateReqDto reqDto,
            @RequestPart(value = "files", required = false) List<MultipartFile> multipartFiles) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(authentication.getName());

        log.info("multipartfile: {}", multipartFiles);
        log.info("req.images: {}", reqDto.getImageUrls());
        try {
            return new BaseResponse<>(templateService.updateTemplate(userId, templateId, reqDto, multipartFiles));
        } catch (ResponseException e) {
            return new BaseResponse<>(e.getErrorCode(), e.getMessage());
        } catch (Exception e) {
            return new BaseResponse<>(ResponseCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @DeleteMapping("/delete/{templateId}")
    public BaseResponse<String> deleteTemplate(@PathVariable Long templateId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(authentication.getName());

        try {
            return new BaseResponse<>(templateService.deleteTemplate(userId, templateId));
        } catch (ResponseException e) {
            return new BaseResponse<>(e.getErrorCode(), e.getMessage());
        } catch (Exception e) {
            return new BaseResponse<>(ResponseCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping("/review-percent/{templateId}")
    public BaseResponse<Integer> getGoodReviewPercent(@PathVariable Long templateId){
        try {
            return new BaseResponse<>(templateService.getGoodReviewPercent(templateId));
        } catch (ResponseException e) {
            return new BaseResponse<>(e.getErrorCode(), e.getMessage());
        } catch (Exception e) {
            return new BaseResponse<>(ResponseCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
