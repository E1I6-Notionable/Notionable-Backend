package com.e1i6.notionable.domain.user.service;

import com.e1i6.notionable.domain.user.data.dto.UserDto;
import com.e1i6.notionable.domain.user.data.dto.response.EmailLoginResDto;
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
public class ProfileService {

    private final UserRepository userRepository;


    public UserDto getMyProfile(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            return UserDto.toUserDto(user);
        } else {
            throw new ResponseException(ResponseCode.NOT_FOUND);
        }
    }
}
