package com.e1i6.notionable.domain.user.service;

import com.e1i6.notionable.domain.community.dto.CommunityRes;
import com.e1i6.notionable.domain.community.entity.Community;
import com.e1i6.notionable.domain.community.repository.CommunityRepository;
import com.e1i6.notionable.domain.user.data.dto.UserDto;
import com.e1i6.notionable.domain.user.entity.User;
import com.e1i6.notionable.domain.user.repository.UserRepository;
import com.e1i6.notionable.global.common.response.ResponseCode;
import com.e1i6.notionable.global.common.response.ResponseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProfileService {

    private final UserRepository userRepository;
    private final CommunityRepository communityRepository;


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
    public UserDto modifyMyProfile(Long userId, UserDto modifyUserDto) {

        modifyUserDto.setUserId(userId);
        Optional<User> optionalUser= userRepository.findById(userId);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.modifyUserProfile(modifyUserDto);

            return UserDto.toUserDto(user);
        } else {
            throw new ResponseException(ResponseCode.NOT_FOUND);
        }
    }

    // 마이페이지 - 내가 쓴 글
    public CommunityRes.CommunityListRes getMyCommunity(Long userId, Pageable pageable) {
        Page<Community> allMyCommunity = communityRepository.findByUser_UserId(userId, pageable);
        return CommunityRes.CommunityListRes.of(allMyCommunity);
    }
}
