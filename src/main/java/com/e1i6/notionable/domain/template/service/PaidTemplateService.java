package com.e1i6.notionable.domain.template.service;

import com.e1i6.notionable.domain.template.data.dto.FreeTemplateDto;
import com.e1i6.notionable.domain.template.data.dto.PaidTemplateDto;
import com.e1i6.notionable.domain.template.data.dto.UploadFreeTemplateReqDto;
import com.e1i6.notionable.domain.template.data.dto.UploadPaidTemplateReqDto;
import com.e1i6.notionable.domain.template.entity.FreeTemplate;
import com.e1i6.notionable.domain.template.entity.PaidTemplate;
import com.e1i6.notionable.domain.template.repository.PaidTemplateRepository;
import com.e1i6.notionable.domain.user.repository.UserRepository;
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
public class PaidTemplateService {
    private final UserRepository userRepository;
    private final AwsS3Service awsS3Service;
    private final PaidTemplateRepository paidTemplateRepository;

    @Transactional
    public String uploadPaidTemplate(
            Long userId,
            List<MultipartFile> multipartFiles,
            UploadPaidTemplateReqDto reqDto) {

        List<String> uploadedUrls = awsS3Service.uploadFiles(multipartFiles);

        paidTemplateRepository.save(PaidTemplate.builder()
                .userId(userId)
                .title(reqDto.getTitle())
                .content(reqDto.getContent())
                .tunmbnail(uploadedUrls.get(0))
                .images(uploadedUrls)
                .price(reqDto.getPrice())
                .notionUrl(reqDto.getNotionUrl())
                .category(reqDto.getCategory())
                .build());

        return "upload success";
    }

    public List<PaidTemplateDto> findRecommendTemplate() {

        List<PaidTemplate> paidTemplates = paidTemplateRepository.findTop5ByOrderByCreatedAtDesc();
        List<PaidTemplateDto> paidTemplateDtos = new ArrayList<>();

        for (PaidTemplate paidTemplate : paidTemplates) {
            PaidTemplateDto resDto = PaidTemplateDto.builder()
                    .paidTemplateId(paidTemplate.getPaidTemplateId())
                    .title(paidTemplate.getTitle())
                    .content(paidTemplate.getContent())
                    .category(paidTemplate.getCategory())
                    .thumbnail(paidTemplate.getTunmbnail())
                    .price(paidTemplate.getPrice())
                    .createdAt(paidTemplate.getCreatedAt().toString())
                    .build();

            paidTemplateDtos.add(resDto);
        }

        return paidTemplateDtos;
    }

    public List<PaidTemplateDto> getPaidTemplatesWithCriteria(
            int pageNo,
            String category,
            String criteria) {

        Pageable pageable = PageRequest.of(pageNo, 9, Sort.Direction.DESC, criteria);
        Page<PaidTemplate> page;
        if (category.equals("all")) {
            page = paidTemplateRepository.findAll(pageable);
        } else {
            page = paidTemplateRepository.findAllByCategory(category, pageable);
        }

        List<PaidTemplateDto> result = new ArrayList<>();
        page.map(paidTemplate -> result.add(PaidTemplateDto.builder()
                .paidTemplateId(paidTemplate.getPaidTemplateId())
                .title(paidTemplate.getTitle())
                .content(paidTemplate.getContent())
                .thumbnail(paidTemplate.getTunmbnail())
                .category(paidTemplate.getCategory())
                .price(paidTemplate.getPrice())
                .createdAt(paidTemplate.getCreatedAt().toString())
                .build()));

        return result;
    }
}
