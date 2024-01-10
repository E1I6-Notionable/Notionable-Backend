package com.e1i6.notionable.domain.creator.service;

import com.e1i6.notionable.domain.creator.dto.CreatorDto;
import com.e1i6.notionable.domain.creator.entity.Creator;
import com.e1i6.notionable.domain.creator.repository.CreatorRepository;
import com.e1i6.notionable.domain.user.entity.User;
import com.e1i6.notionable.domain.user.repository.UserRepository;
import com.e1i6.notionable.global.common.response.ResponseCode;
import com.e1i6.notionable.global.common.response.ResponseException;
import com.e1i6.notionable.global.service.AwsS3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreatorService {

    private final AwsS3Service awsS3Service;
    private final CreatorRepository creatorRepository;
    private final UserRepository userRepository;

    public CreatorDto creatorRegister(Long userId, CreatorDto creatorDto, MultipartFile bankPaper, MultipartFile identification) {
        Optional<Creator> optionalCreator = creatorRepository.findByUserUserId(userId);
        Optional<User> optionalUser = userRepository.findById(userId);
        String bankPaperUrl = awsS3Service.getUrlFromFileName(awsS3Service.uploadFile(bankPaper));
        String identificationUrl = awsS3Service.getUrlFromFileName(awsS3Service.uploadFile(identification));


        if (optionalCreator.isPresent()){
            Creator creator = optionalCreator.get();
            if(creator.getStatus() == "pending")
                return CreatorDto.toCreatorDto(creator);
            else
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
                        .status("pending")
                        .user(user)
                        .build();

                creatorRepository.save(creator);

                return CreatorDto.toCreatorDto(creator);
            } else {
                throw new ResponseException(ResponseCode.NOT_FOUND);
            }
        }
    }

}

