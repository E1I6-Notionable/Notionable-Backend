package com.e1i6.notionable.domain.user.service;

import com.e1i6.notionable.domain.user.data.dto.UserDto;
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
public class ProfileService {

    private final UserRepository userRepository;
    private final AwsS3Service awsS3Service;

    public UserDto getMyProfile(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            return UserDto.toUserDto(user);
        } else {
            throw new ResponseException(ResponseCode.NOT_FOUND);
        }
    }

    @Transactional
    public UserDto modifyMyProfile(Long userId, UserDto modifyUserDto, MultipartFile newProfile) {

        String newProfileUrl = awsS3Service.getUrlFromFileName(awsS3Service.uploadFile(newProfile));
        modifyUserDto.setUserId(userId);
        modifyUserDto.setProfile(newProfileUrl);
        Optional<User> optionalUser= userRepository.findById(userId);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.modifyUserProfile(modifyUserDto);

            return UserDto.toUserDto(user);
        } else {
            throw new ResponseException(ResponseCode.NOT_FOUND);
        }
    }
}
