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
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CreatorService {

    private final CreatorRepository creatorRepository;
    private final UserRepository userRepository;
    public CreatorDto creatorRegister(Long userId, CreatorDto creatorDto) {

        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            Creator creator = Creator.builder()
                    .user(user)
                    .creatorType("개인사업자")
                    .bank("KB국민은행")
                    .accountNumber("512602-01-201673")
                    .bankPaperUrl("")
                    .identificationUrl("")
                    .status("pending")
                    .build();

            creatorRepository.save(creator);


        } else {
            throw new ResponseException(ResponseCode.NOT_FOUND);
        }
    }
}
