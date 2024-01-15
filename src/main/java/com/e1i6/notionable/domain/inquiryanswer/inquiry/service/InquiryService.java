package com.e1i6.notionable.domain.inquiryanswer.inquiry.service;

import com.e1i6.notionable.domain.creator.repository.CreatorRepository;
import com.e1i6.notionable.domain.inquiryanswer.InquiryAnswerDto;
import com.e1i6.notionable.domain.inquiryanswer.answer.dto.AnswerDto;
import com.e1i6.notionable.domain.inquiryanswer.answer.entity.Answer;
import com.e1i6.notionable.domain.inquiryanswer.answer.repository.AnswerRepository;
import com.e1i6.notionable.domain.inquiryanswer.inquiry.dto.InquiryDto;
import com.e1i6.notionable.domain.inquiryanswer.inquiry.entity.Inquiry;
import com.e1i6.notionable.domain.inquiryanswer.inquiry.repository.InquiryRepository;
import com.e1i6.notionable.domain.template.entity.Template;
import com.e1i6.notionable.domain.template.repository.TemplateRepository;
import com.e1i6.notionable.domain.user.entity.User;
import com.e1i6.notionable.domain.user.repository.UserRepository;
import com.e1i6.notionable.global.service.AwsS3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service
public class InquiryService {

    private final InquiryRepository inquiryRepository;
    private final AnswerRepository answerRepository;
    private final UserRepository userRepository;
    private final TemplateRepository templateRepository;
    private final AwsS3Service awsS3Service;

    // 문의 작성
    @Transactional
    public InquiryDto writeInquiry(Long userId, InquiryDto inquiryDto, MultipartFile file) {
        Optional<User> optionalUser = userRepository.findById(userId);
        Optional<Template> optionalTemplate = templateRepository.findById(inquiryDto.getTemplate_id());

        User user = null;
        Template template = null;
        if(optionalUser.isPresent() && optionalTemplate.isPresent()){
            user = optionalUser.get();
            template = optionalTemplate.get();
        }


        String fileUrl = awsS3Service.getUrlFromFileName(awsS3Service.uploadFile(file));

        assert template != null;
        Inquiry inquiry = Inquiry.builder()
                .title(inquiryDto.getTitle())
                .content(inquiryDto.getContent())
                .fileUrl(fileUrl)
                .createdAt(LocalDateTime.now())
                .status("No")
                .template_id(inquiryDto.getTemplate_id())
                .creator_id(template.getUser().getCreator().getCreator_id())
                .user(user)
                .build();

        inquiryRepository.save(inquiry);

        return InquiryDto.toInquiryDto(inquiry);
    }

    public List<InquiryAnswerDto> getAllInquiryAnswer(Long userId) {
        List<Inquiry> inquiryEntityList = inquiryRepository.findByUser_UserId(userId);
        List<InquiryAnswerDto> inquiryAnswerDtoList = new ArrayList<>();

        for (Inquiry inquiry : inquiryEntityList) {
            InquiryAnswerDto inquiryAnswerDto = new InquiryAnswerDto();

            // 문의
            inquiryAnswerDto.setInquiry(InquiryDto.toInquiryDto(inquiry));

            Optional<Answer> optionalAnswer = answerRepository.findAnswerByInquiryId(inquiry.getInquiry_id());

            if(optionalAnswer.isPresent()){
                inquiryAnswerDto.setAnswer(AnswerDto.toAnswerDto(optionalAnswer.get()));
            }
            else{
                inquiryAnswerDto.setAnswer(null);
            }

            inquiryAnswerDtoList.add(inquiryAnswerDto);
        }

        return inquiryAnswerDtoList;
    }
}
