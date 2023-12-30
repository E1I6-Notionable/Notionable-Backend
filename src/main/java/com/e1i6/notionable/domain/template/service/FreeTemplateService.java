package com.e1i6.notionable.domain.template.service;

import com.e1i6.notionable.domain.template.data.dto.FreeTemplateDetailDto;
import com.e1i6.notionable.domain.template.data.dto.FreeTemplateDto;
import com.e1i6.notionable.domain.template.data.dto.FreeTemplateDto;
import com.e1i6.notionable.domain.template.data.dto.UploadFreeTemplateReqDto;
import com.e1i6.notionable.domain.template.entity.FreeTemplate;
import com.e1i6.notionable.domain.template.repository.FreeTemplateRepository;
import com.e1i6.notionable.domain.user.entity.User;
import com.e1i6.notionable.domain.user.repository.UserRepository;
import com.e1i6.notionable.global.common.response.ResponseCode;
import com.e1i6.notionable.global.common.response.ResponseException;
import com.e1i6.notionable.global.service.AwsS3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FreeTemplateService {

    private final UserRepository userRepository;
    private final AwsS3Service awsS3Service;
    private final FreeTemplateRepository freeTemplateRepository;

    @Transactional
    public String uploadFreeTemplate(
            Long userId,
            List<MultipartFile> multipartFiles,
            UploadFreeTemplateReqDto reqDto) {

        List<String> uploadedUrls = awsS3Service.uploadFiles(multipartFiles);

        freeTemplateRepository.save(FreeTemplate.builder()
                .userId(userId)
                .title(reqDto.getTitle())
                .content(reqDto.getContent())
                .tunmbnail(uploadedUrls.get(0))
                .images(uploadedUrls)
                .category(reqDto.getCategory())
                .notionUrl(reqDto.getNotionUrl())
                .build());

        return "upload success";
    }

    public List<FreeTemplateDto> getRecommendTemplate() {

        List<FreeTemplate> freeTemplates = freeTemplateRepository.findTop5ByOrderByCreatedAtDesc();
        List<FreeTemplateDto> freeTemplateDtos = new ArrayList<>();

        for (FreeTemplate freeTemplate : freeTemplates) {
            User user = userRepository.findById(freeTemplate.getUserId()).get();

            FreeTemplateDto resDto = FreeTemplateDto.builder()
                    .freeTemplateId(freeTemplate.getFreeTemplateId())
                    .nickName(user.getNickName())
                    .profile(user.getProfile())
                    .title(freeTemplate.getTitle())
                    .category(freeTemplate.getCategory())
                    .thumbnail(freeTemplate.getTunmbnail())
                    .createdAt(freeTemplate.getCreatedAt().toString())
                    .build();

            freeTemplateDtos.add(resDto);
        }

        return freeTemplateDtos;
    }

    public List<FreeTemplateDto> getFreeTemplatesWithCriteria(
            int pageNo,
            String category,
            String criteria) {

        Pageable pageable = PageRequest.of(pageNo, 9, Sort.Direction.DESC, criteria);
        Page<FreeTemplate> page;
        if (category.equals("all")) {
            page = freeTemplateRepository.findAll(pageable);
        } else {
            page = freeTemplateRepository.findAllByCategory(category, pageable);
        }

        List<FreeTemplateDto> result = new ArrayList<>();
        for (FreeTemplate freeTemplate : page) {
            User user = userRepository.findById(freeTemplate.getUserId()).get();

            result.add(FreeTemplateDto.builder()
                    .freeTemplateId(freeTemplate.getFreeTemplateId())
                    .nickName(user.getNickName())
                    .profile(user.getProfile())
                    .title(freeTemplate.getTitle())
                    .thumbnail(freeTemplate.getTunmbnail())
                    .category(freeTemplate.getCategory())
                    .createdAt(freeTemplate.getCreatedAt().toString())
                    .build());
        }

        return result;
    }

    public FreeTemplateDetailDto getFreeTemplateDetail(Long freeTemplateId) {
        FreeTemplate freeTemplate = freeTemplateRepository.findById(freeTemplateId)
                .orElseThrow(() -> new ResponseException(ResponseCode.NO_SUCH_TEMPLATE));
        User user = userRepository.findById(freeTemplate.getUserId())
                .orElseThrow(() -> new ResponseException(ResponseCode.NO_SUCH_USER));

        return FreeTemplateDetailDto.builder()
                .freeTemplateId(freeTemplateId)
                .nickName(user.getNickName())
                .profile(user.getProfile())
                .thumbnail(freeTemplate.getTunmbnail())
                .title(freeTemplate.getTitle())
                .content(freeTemplate.getContent())
                .images(freeTemplate.getImages())
                .category(freeTemplate.getCategory())
                .createdAt(freeTemplate.getCreatedAt().toString())
                .build();
    }
}
