package com.e1i6.notionable.domain.creator.service;

import com.e1i6.notionable.domain.creator.dto.CreatorDto;
import com.e1i6.notionable.domain.creator.entity.Creator;
import com.e1i6.notionable.domain.creator.repository.CreatorRepository;
import com.e1i6.notionable.domain.user.data.dto.UserDto;
import com.e1i6.notionable.domain.user.entity.User;
import com.e1i6.notionable.domain.user.repository.UserRepository;
import com.e1i6.notionable.global.common.response.ResponseCode;
import com.e1i6.notionable.global.common.response.ResponseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreatorService {

    private final CreatorRepository creatorRepository;
    private final UserRepository userRepository;

    public CreatorDto creatorRegister(Long userId, CreatorDto creatorDto) {
        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            log.info("creatorDto = {}", creatorDto);

            Creator creator = Creator.builder()
                    .creatorType(creatorDto.getCreatorType())
                    .bank(creatorDto.getBank())
                    .accountNumber(creatorDto.getAccountNumber())
                    .bankPaperUrl(creatorDto.getBankPaperUrl())
                    .identificationUrl(creatorDto.getIdentificationUrl())
                    .status(creatorDto.getStatus())
                    .user(user)
                    .build();

            creatorRepository.save(creator);

            return CreatorDto.toCreatorDto(creator);
        } else {
            throw new ResponseException(ResponseCode.NOT_FOUND);
        }
    }

}

