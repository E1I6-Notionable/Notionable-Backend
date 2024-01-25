package com.e1i6.notionable.domain.creator.service;

import com.e1i6.notionable.domain.creator.dto.CreatorDto;
import com.e1i6.notionable.domain.creator.dto.CreatorListCountDto;
import com.e1i6.notionable.domain.creator.dto.VerifyCreatorDto;
import com.e1i6.notionable.domain.creator.entity.Creator;
import com.e1i6.notionable.domain.creator.repository.CreatorRepository;
import com.e1i6.notionable.domain.inquiryanswer.answer.repository.AnswerRepository;
import com.e1i6.notionable.domain.inquiryanswer.inquiry.repository.InquiryRepository;
import com.e1i6.notionable.domain.payment.repository.PaymentRepository;
import com.e1i6.notionable.domain.user.data.dto.UserDto;
import com.e1i6.notionable.domain.user.entity.Role;
import com.e1i6.notionable.domain.user.entity.User;
import com.e1i6.notionable.domain.user.repository.UserRepository;
import com.e1i6.notionable.global.common.response.ResponseCode;
import com.e1i6.notionable.global.common.response.ResponseException;
import com.e1i6.notionable.global.service.AwsS3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreatorService {

    private final AwsS3Service awsS3Service;
    private final CreatorRepository creatorRepository;
    private final UserRepository userRepository;
    private final PaymentRepository paymentRepository;
    private final AnswerRepository answerRepository;
    private final InquiryRepository inquiryRepository;

    @Transactional
    public CreatorDto creatorRegister(Long userId, CreatorDto creatorDto, MultipartFile bankPaper, MultipartFile identification) {
        Optional<Creator> optionalCreator = creatorRepository.findByUserUserId(userId);
        Optional<User> optionalUser = userRepository.findById(userId);
        String bankPaperUrl = awsS3Service.getUrlFromFileName(awsS3Service.uploadFile(bankPaper));
        String identificationUrl = awsS3Service.getUrlFromFileName(awsS3Service.uploadFile(identification));


        if (optionalCreator.isPresent()){
            Creator creator = optionalCreator.get();
//            if(creator.getStatus() == "waited")
//                return CreatorDto.toCreatorDto(creator);
//            else
            return null;
        }
        else{
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();

                log.info("creatorDto = {}", creatorDto);

                Creator creator = Creator.builder()
                        .creatorType(creatorDto.getCreatorType())
                        .bank(creatorDto.getBank())
                        .accountNumber(creatorDto.getAccountNumber())
                        .bankPaper(bankPaperUrl)
                        .identification(identificationUrl)
                        //.status("waited")
                        .status("accepted")
                        .user(user)
                        .role(Role.ROLE_CREATOR)
                        .build();

                creatorRepository.save(creator);

                return CreatorDto.toCreatorDto(creator);
            } else {
                throw new ResponseException(ResponseCode.NOT_FOUND);
            }
        }
    }

    public CreatorDto changeToCreator(Long userId) {
        Optional<Creator> optionalCreator = creatorRepository.findByUserUserId(userId);
        if (optionalCreator.isPresent()){
            Creator creator = optionalCreator.get();

            if(creator.getStatus().equals("accepted"))
                return CreatorDto.toCreatorDto(creator);
            else
                return null;
        }
        else
            return null;
    }

    public UserDto changeToUser(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            return UserDto.toUserDto(user);
        } else {
            throw new ResponseException(ResponseCode.NOT_FOUND);
        }
    }

    public VerifyCreatorDto verifyCreator(Long userId) {
        Optional<Creator> optionalCreator = creatorRepository.findByUserUserId(userId);
        VerifyCreatorDto verifyCreatorDto = new VerifyCreatorDto();
        if(optionalCreator.isPresent())
            verifyCreatorDto.setIsCreator(Boolean.TRUE);
        else
            verifyCreatorDto.setIsCreator(Boolean.FALSE);

        return verifyCreatorDto;
    }

    public CreatorListCountDto returnCreatorListCount(Long userId) {
        Optional<Creator> optionalCreator = creatorRepository.findByUserUserId(userId);
        CreatorListCountDto creatorListCountDto = new CreatorListCountDto();
        creatorListCountDto.setSellCount(0L);
        creatorListCountDto.setAnswerCount(0L);
        creatorListCountDto.setInquiryCount(0L);

        if (optionalCreator.isPresent()) {
            Creator creator = optionalCreator.get();
            creatorListCountDto.setSellCount(paymentRepository.countAllBySellerId(userId));
            creatorListCountDto.setAnswerCount(answerRepository.countAnswersByCreatorId(creator.getCreator_id()));
            creatorListCountDto.setInquiryCount(inquiryRepository.countInquiryByCreatorId(creator.getCreator_id()));

            return creatorListCountDto;
        } else {
            throw new ResponseException(ResponseCode.NOT_FOUND);
        }
    }
}

