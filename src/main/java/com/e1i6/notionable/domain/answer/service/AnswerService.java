package com.e1i6.notionable.domain.answer.service;

import com.e1i6.notionable.domain.answer.dto.AnswerDto;
import com.e1i6.notionable.domain.answer.entity.Answer;
import com.e1i6.notionable.domain.answer.repository.AnswerRepository;
import com.e1i6.notionable.domain.creator.entity.Creator;
import com.e1i6.notionable.domain.creator.repository.CreatorRepository;
import com.e1i6.notionable.domain.inquiry.entity.Inquiry;
import com.e1i6.notionable.domain.inquiry.repository.InquiryRepository;
import com.e1i6.notionable.domain.user.entity.User;
import com.e1i6.notionable.global.service.AwsS3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service
public class AnswerService{

    private final AnswerRepository answerRepository;
    private final CreatorRepository creatorRepository;
    private final InquiryRepository inquiryRepository;
    private final AwsS3Service awsS3Service;

    @Transactional
    public AnswerDto writeAnswer(Long userId, AnswerDto answerDto, MultipartFile file) {
        Optional<Inquiry> optionalInquiry = inquiryRepository.findById(answerDto.getInquiry_id());
        Inquiry inquiry = null;
        Optional<Creator> optionalCreator = creatorRepository.findByUserUserId(userId);
        Creator creator = null;
        if(optionalInquiry.isPresent() && optionalCreator.isPresent()){
            inquiry = optionalInquiry.get();
            creator = optionalCreator.get();
        }


        String fileUrl = awsS3Service.getUrlFromFileName(awsS3Service.uploadFile(file));

        Answer answer = Answer.builder()
                .template_id(answerDto.getTemplate_id())
                .title(answerDto.getTitle())
                .content(answerDto.getContent())
                .createdAt(LocalDateTime.now())
                .status("Yes")
                .fileUrl(fileUrl)
                .inquiry(inquiry)
                .creator(creator)
                .build();

        answerRepository.save(answer);

        return AnswerDto.toAnswerDto(answer);
    }
}
