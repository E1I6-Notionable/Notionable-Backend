package com.e1i6.notionable.domain.template.service;

import com.e1i6.notionable.domain.payment.repository.PaymentRepository;
import com.e1i6.notionable.domain.payment.service.PaymentService;
import com.e1i6.notionable.domain.template.data.*;
import com.e1i6.notionable.domain.template.entity.Template;
import com.e1i6.notionable.domain.template.repository.TemplateRepository;
import com.e1i6.notionable.domain.user.entity.User;
import com.e1i6.notionable.domain.user.repository.UserRepository;
import com.e1i6.notionable.domain.usermailauth.service.MailService;
import com.e1i6.notionable.global.common.response.ResponseCode;
import com.e1i6.notionable.global.common.response.ResponseException;
import com.e1i6.notionable.global.service.AwsS3Service;
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
    private final MailService mailService;
    private final TemplateRepository templateRepository;
    private final PaymentService paymentService;

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
            TemplateUploadReqDto reqDto) {

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
                .goodRateCount(0)
                .category(reqDto.getCategory())
                .build());

        return "template upload success";
    }

    public List<TemplateDto> getRecommendFreeTemplates() {
        // "만족해요" 평가가 가장 많은 리뷰 수를 가진 무료 템플릿 5개
        List<Template> templateList = templateRepository.findTop5ByPriceEqualsOrderByGoodRateCountDesc(0);
        List<TemplateDto> templateDtoList = new ArrayList<>();
        templateList.forEach(template -> templateDtoList.add(Template.toTemplateDto(template)));

        return templateDtoList;
    }

    public List<TemplateDto> getRecommendPaidTemplates() {
        // "만족해요" 평가가 가장 많은 리뷰 수를 가진 유료 템플릿 5개
        List<Template> templateList = templateRepository.findTop5ByPriceGreaterThanOrderByGoodRateCountDesc(0);
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

    public TemplateDetailDto getTemplateDetail(Long userId, Long templateId) {
        Template template = templateRepository.findById(templateId)
                .orElseThrow(() -> new ResponseException(ResponseCode.NO_SUCH_TEMPLATE));

        boolean isPaid = false;
        if (userId != null) {
            log.info("userId: {}", userId);
            if (template.getPrice() == 0) {
                isPaid = true;
            } else {
                isPaid = paymentService.isPaidUser(userId, templateId);
            }
        }

        List<String> imageUrlList = new ArrayList<>();
        template.getImages().forEach(image -> imageUrlList.add(awsS3Service.getUrlFromFileName(image)));

        return Template.toDetailTemplateDto(template, imageUrlList, isPaid);
    }

    @Transactional
    public String updateTemplate(
            Long userId,
            Long templateId,
            TemplateUpdateReqDto reqDto,
            List<MultipartFile> multipartFiles) {
        // 이미지가 하나도 없을 때
        if (reqDto.getImageUrls().isEmpty() && multipartFiles == null) {
            throw new ResponseException(ResponseCode.NO_IMAGES);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseException(ResponseCode.NO_SUCH_USER));
        Template template = templateRepository.findById(templateId)
                .orElseThrow(() -> new ResponseException(ResponseCode.NO_SUCH_TEMPLATE));

        if (user.getUserId() != template.getUser().getUserId()) {
            throw new ResponseException(ResponseCode.NO_AUTHORIZATION);
        }

        if (reqDto.getImageUrls().isEmpty()) {
            // s3에 업로드된 파일 삭제
            template.getImages().forEach(awsS3Service::deleteFile);

            // 새로 사진 업로드
            List<String> uploadedFileNames = awsS3Service.uploadFiles(multipartFiles);
            String thumbnail = awsS3Service.getUrlFromFileName(uploadedFileNames.get(0));

            template.updateTemplate(new TemplateUpdateDto(reqDto, thumbnail, uploadedFileNames));
        }
        else {
            List<String> newImages = new ArrayList<>();
            List<String> beforeImages = template.getImages();

            // 기존의 사진이 사라졌다면 삭제
            reqDto.getImageUrls().forEach(imageUrl -> {
                String fileName = awsS3Service.getFileNameFromUrl(imageUrl);

                if (beforeImages.contains(fileName)) {
                    log.info("added file: {}", fileName);
                    newImages.add(fileName);
                }
                else {
                    awsS3Service.deleteFile(fileName);
                    log.info("deleted file: {}", fileName);
                }
            });

            // 새로 추가된 사진이 있을 떄
            if (multipartFiles != null) {
                List<String> uploadedFileNames = awsS3Service.uploadFiles(multipartFiles);
                newImages.addAll(uploadedFileNames);
            }

            String thumbnail = template.getThumbnail();
            template.updateTemplate(new TemplateUpdateDto(reqDto, thumbnail, newImages));
        }

        templateRepository.save(template);
        return "template update success";
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
        template.getImages().forEach(awsS3Service::deleteFile);

        templateRepository.delete(template);
        return "template delete success";
    }

    public Integer getGoodReviewPercent(Long templateId) {
        Template template = templateRepository.findById(templateId)
                .orElseThrow(() -> new ResponseException(ResponseCode.NO_SUCH_TEMPLATE));

        Integer reviewCount = template.getReviewList().size();
        if (reviewCount == 0)
            return 0;

        return ((template.getGoodRateCount() * 100) / reviewCount);
    }

    public String getNotionUrlEmail(Long userId, Long templateId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseException(ResponseCode.NO_SUCH_USER));
        String email = user.getEmail();

        Template template = templateRepository.findById(templateId)
                .orElseThrow(() -> new ResponseException(ResponseCode.NO_SUCH_TEMPLATE));
        String notionUrl = template.getNotionUrl();
        log.info("send notion url: {}", notionUrl);
        try {
            return mailService.sendNotionUrlEmail(email, notionUrl);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
