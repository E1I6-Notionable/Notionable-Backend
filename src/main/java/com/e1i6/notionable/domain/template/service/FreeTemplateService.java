package com.e1i6.notionable.domain.template.service;

import com.e1i6.notionable.domain.template.data.dto.FreeTemplateResDto;
import com.e1i6.notionable.domain.template.data.dto.UploadFreeTemplateReqDto;
import com.e1i6.notionable.domain.template.entity.FreeTemplate;
import com.e1i6.notionable.domain.template.repository.FreeTemplateRepository;
import com.e1i6.notionable.domain.user.repository.UserRepository;
import com.e1i6.notionable.global.service.AwsS3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FreeTemplateService {

    private final UserRepository userRepository;
    private final AwsS3Service awsS3Service;
    private final FreeTemplateRepository freeTemplateRepository;

    @Transactional
    public String uploadFreeTemplate(Long userId, List<MultipartFile> multipartFiles, UploadFreeTemplateReqDto reqDto) {
        List<String> uploadedUrls = awsS3Service.uploadFiles(multipartFiles);

        freeTemplateRepository.save(FreeTemplate.builder()
                .userId(userId)
                .title(reqDto.getTitle())
                .content(reqDto.getContent())
                .tunmbnail(uploadedUrls.get(0))
                .images(uploadedUrls)
                .category(reqDto.getCategory())
                .build());

        return "upload success";
    }

    public List<FreeTemplateResDto> findRecommendTemplate() {
        return null;
    }

}
