package com.e1i6.notionable.domain.inquiryanswer.answer.service;

import com.e1i6.notionable.domain.inquiryanswer.InquiryAnswerDto;
import com.e1i6.notionable.domain.inquiryanswer.answer.dto.AnswerDto;
import com.e1i6.notionable.domain.inquiryanswer.answer.entity.Answer;
import com.e1i6.notionable.domain.inquiryanswer.answer.repository.AnswerRepository;
import com.e1i6.notionable.domain.creator.entity.Creator;
import com.e1i6.notionable.domain.creator.repository.CreatorRepository;
import com.e1i6.notionable.domain.inquiryanswer.inquiry.dto.InquiryDto;
import com.e1i6.notionable.domain.inquiryanswer.inquiry.entity.Inquiry;
import com.e1i6.notionable.domain.inquiryanswer.inquiry.repository.InquiryRepository;
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
public class AnswerService{

    private final AnswerRepository answerRepository;
    private final CreatorRepository creatorRepository;
    private final InquiryRepository inquiryRepository;
    private final UserRepository userRepository;
    private final AwsS3Service awsS3Service;

    @Transactional
    public AnswerDto writeAnswer(Long userId, AnswerDto answerDto, MultipartFile file) {
        Optional<Inquiry> optionalInquiry = inquiryRepository.findById(answerDto.getInquiry_id());
        Inquiry inquiry = null;
        Optional<Creator> optionalCreator = creatorRepository.findByUserUserId(userId);
        Creator creator = null;
        if(optionalInquiry.isPresent() && optionalCreator.isPresent()){
            inquiry = optionalInquiry.get();
            inquiry.modifyInquiryStatus("Yes");
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

    public List<InquiryAnswerDto> getAllInquiryAnswer(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        Long creatorId = null;
        if(optionalUser.isPresent()){
            creatorId = optionalUser.get().getCreator().getCreator_id();
        }
        log.info("creator_id = {}", creatorId);
        List<Inquiry> inquiryEntityList = inquiryRepository.findInquiriesByCreatorId(creatorId);
        List<InquiryAnswerDto> inquiryAnswerDtoList = new ArrayList<>();
        log.info("test = {}", inquiryEntityList.get(0).getCreator_id());
        for (Inquiry inquiry : inquiryEntityList) {
            InquiryAnswerDto inquiryAnswerDto = new InquiryAnswerDto();
            log.info("content = {}", inquiry.getContent());
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
