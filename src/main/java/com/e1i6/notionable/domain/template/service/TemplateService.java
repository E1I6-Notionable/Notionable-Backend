package com.e1i6.notionable.domain.template.service;

import com.e1i6.notionable.domain.template.data.dto.*;
import com.e1i6.notionable.domain.template.entity.Template;
import com.e1i6.notionable.domain.template.repository.TemplateRepository;
import com.e1i6.notionable.domain.user.entity.User;
import com.e1i6.notionable.domain.user.repository.UserRepository;
import com.e1i6.notionable.global.common.response.BaseResponse;
import com.e1i6.notionable.global.common.response.ResponseCode;
import com.e1i6.notionable.global.common.response.ResponseException;
import com.e1i6.notionable.global.service.AwsS3Service;
import com.mysql.cj.PreparedQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class TemplateService {
    private final UserRepository userRepository;
    private final AwsS3Service awsS3Service;
    private final TemplateRepository templateRepository;

    private final List<String> categoryList = Arrays.asList(
            "", // all
            "studyManagement",
            "plan",
            "account",
            "business",
            "hobby",
            "habit",
            "reading",
            "travel"
    );

    private final List<String> criteriaList = Arrays.asList(
            "createdAt",
            "price"
    );

    @Transactional
    public String uploadTemplate(
            Long userId,
            List<MultipartFile> multipartFiles,
            UploadTemplateReqDto reqDto) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseException(ResponseCode.NO_SUCH_USER));

        List<String> uploadedFileNames = awsS3Service.uploadFiles(multipartFiles);
        String thumbnailUrl = awsS3Service.getUrlFromFileName(uploadedFileNames.get(0));

        templateRepository.save(Template.builder()
                .user(user)
                .title(reqDto.getTitle())
                .content(reqDto.getContent())
                .thumbnail(thumbnailUrl)
                .images(uploadedFileNames)
                .price(reqDto.getPrice())
                .notionUrl(reqDto.getNotionUrl())
                .category(reqDto.getCategory())
                .build());

        return "template upload success";
    }

    public List<TemplateDto> getRecommendFreeTemplates() {
        // 가장 최근 무료 템플릿 5개
        List<Template> templateList = templateRepository.findRecentFree();
        List<TemplateDto> templateDtoList = new ArrayList<>();
        templateList.forEach(template -> templateDtoList.add(Template.toTemplateDto(template)));

        return templateDtoList;
    }

    public List<TemplateDto> getRecommendPaidTemplates() {
        // 가장 최근 유료 템플릿 5개
        List<Template> templateList = templateRepository.findRecentPaid();
        List<TemplateDto> templateDtoList = new ArrayList<>();
        templateList.forEach(template -> templateDtoList.add(Template.toTemplateDto(template)));

        return templateDtoList;
    }

    public List<TemplateDto> getTemplatesWithFilter(
            int pageNo,
            String keyword,
            String templateType,
            String category,
            String criteria,
            String criteriaOption) {

        if (!categoryList.contains(category))
            throw new ResponseException(ResponseCode.NO_SUCH_CATEGORY);
        if (!criteriaList.contains(criteria))
            throw new ResponseException(ResponseCode.WRONG_CRITERIA);
        if (!criteriaOption.equals("desc") && !criteriaOption.equals("asc"))
            throw new ResponseException(ResponseCode.WRONG_CRITERIA_OPTION);

        Pageable pageable;
        if (criteriaOption.equals("asc")) {
            pageable = PageRequest.of(pageNo, 9, Sort.Direction.ASC, criteria);
        } else {
            pageable = PageRequest.of(pageNo, 9, Sort.Direction.DESC, criteria);
        }

        Page<Template> page;
        // 모든 템플릿
        if (templateType.isEmpty())
            page = templateRepository.findTemplateWithFilter(category, keyword, pageable);
        // 무료
        else if (templateType.equals("free"))
            page = templateRepository.findFreeTemplateWithFilter(category, keyword, pageable);
        // 유료
        else
            page = templateRepository.findPaidTemplateWithFilter(category, keyword, pageable);

        List<TemplateDto> templateDtoList = new ArrayList<>();
        page.map(template -> templateDtoList.add(Template.toTemplateDto(template)));

        return templateDtoList;
    }
/*
    public List<TemplateDto> getFreeTemplatesWithCriteria(
            int pageNo,
            String category,
            String criteria,
            String criteriaOption) {

        if (!categoryList.contains(category))
            throw new ResponseException(ResponseCode.NO_SUCH_CATEGORY);

        Pageable pageable = PageRequest.of(pageNo, 9, Sort.Direction.DESC, criteria);
        Page<Template> page;
        if (category.equals("all")) {
            page = templateRepository.findAllByPriceEquals(0, pageable);
        } else {
            page = templateRepository.findAllByCategoryAndPriceEquals(category, 0, pageable);
        }

        List<TemplateDto> templateDtoList = new ArrayList<>();
        page.map(template -> templateDtoList.add(Template.toTemplateDto(template)));

        return templateDtoList;
    }

    public List<TemplateDto> getPaidTemplatesWithCriteria(
            int pageNo,
            String category,
            String criteria,
            String criteriaOption) {

        if (!categoryList.contains(category))
            throw new ResponseException(ResponseCode.NO_SUCH_CATEGORY);

        Pageable pageable = PageRequest.of(pageNo, 9, Sort.Direction.DESC, criteria);
        Page<Template> page;
        if (category.equals("all")) {
            page = templateRepository.findAllByPriceGreaterThan(0, pageable);
        } else {
            page = templateRepository.findAllByCategoryAndPriceGreaterThan(category, 0, pageable);
        }

        List<TemplateDto> templateDtoList = new ArrayList<>();
        page.map(template -> templateDtoList.add(Template.toTemplateDto(template)));

        return templateDtoList;
    }*/

    public TemplateDetailDto getTemplateDetail(Long templateId) {
        Template template = templateRepository.findById(templateId)
                .orElseThrow(() -> new ResponseException(ResponseCode.NO_SUCH_TEMPLATE));

        List<String> imageUrlList = new ArrayList<>();
        template.getImages().forEach(image -> imageUrlList.add(awsS3Service.getUrlFromFileName(image)));

        return Template.toDetailTemplateDto(template, imageUrlList);
    }

    @Transactional
    public String deleteTemplate(Long userId, Long templateId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseException(ResponseCode.NO_SUCH_USER));
        Template template = templateRepository.findById(templateId)
                .orElseThrow(() -> new ResponseException(ResponseCode.NO_SUCH_TEMPLATE));

        if (user.getUserId() != template.getUser().getUserId()) {
            throw new ResponseException(ResponseCode.NO_AUTHORIZATION);
        }

        // s3에 업로드된 파일 삭제
        for (String fileName : template.getImages()) {
            awsS3Service.deleteFile(fileName);
        }

        templateRepository.delete(template);
        return "template delete success";
    }
}
