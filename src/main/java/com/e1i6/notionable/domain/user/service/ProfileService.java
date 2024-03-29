package com.e1i6.notionable.domain.user.service;

import com.e1i6.notionable.domain.community.dto.CommunityRes;
import com.e1i6.notionable.domain.community.entity.Community;
import com.e1i6.notionable.domain.community.repository.CommunityRepository;
import com.e1i6.notionable.domain.community.repository.LikeRepository;
import com.e1i6.notionable.domain.inquiryanswer.inquiry.repository.InquiryRepository;
import com.e1i6.notionable.domain.payment.repository.PaymentRepository;
import com.e1i6.notionable.domain.user.data.dto.ListCountDto;
import com.e1i6.notionable.domain.user.data.dto.UserDto;
import com.e1i6.notionable.domain.user.entity.User;
import com.e1i6.notionable.domain.user.repository.UserRepository;
import com.e1i6.notionable.global.common.response.ResponseCode;
import com.e1i6.notionable.global.common.response.ResponseException;
import com.e1i6.notionable.global.service.AwsS3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProfileService {

    private final UserRepository userRepository;
    private final CommunityRepository communityRepository;
    private final PaymentRepository paymentRepository;
    private final InquiryRepository inquiryRepository;
    private final LikeRepository likeRepository;
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

    public ListCountDto getListCount(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseException(ResponseCode.NO_SUCH_USER));

        return ListCountDto.builder()
                .paymentCount(paymentRepository.countAllByBuyerId(userId))
                .postCount(communityRepository.countAllByUser(user))
                .inquiryCount(inquiryRepository.countAllByUser(user))
                .build();
    }
}
