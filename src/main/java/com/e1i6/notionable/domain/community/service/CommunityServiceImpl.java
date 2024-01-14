package com.e1i6.notionable.domain.community.service;

import com.e1i6.notionable.domain.community.dto.CommunityReq;
import com.e1i6.notionable.domain.community.dto.CommunityRes;
import com.e1i6.notionable.domain.community.entity.Community;
import com.e1i6.notionable.domain.community.repository.CommunityRepository;
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

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommunityServiceImpl implements CommunityService{
    private final CommunityRepository communityRepository;
    private final AwsS3Service awsS3Service;
    private final UserRepository userRepository;

    //게시글 목록 조회
    public CommunityRes.CommunityListRes getCommunity(String keyword, String filter, Pageable pageable) {
        Page<Community> allCommunity = communityRepository.findByKeywordAndFilter(keyword, filter, pageable);
        return CommunityRes.CommunityListRes.of(allCommunity);
    }


    //게시글 작성
    public Long addCommunity(Long userId, List<MultipartFile> multipartFiles, CommunityReq communityReq) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        List<String> uploadedFileNames = new ArrayList<>();
        String thumbnailUrl = null;

        if (multipartFiles != null && !multipartFiles.isEmpty()) {
            uploadedFileNames = awsS3Service.uploadFiles(multipartFiles);
            thumbnailUrl = awsS3Service.getUrlFromFileName(uploadedFileNames.get(0));
        }

        Community community = Community.builder()
                .category(communityReq.getCategory())
                .images(uploadedFileNames)
                .thumbnail(thumbnailUrl)
                .communityLike(0L)
                .communityComment(0L)
                .title(communityReq.getTitle())
                .content(communityReq.getContent())
                .user(user)
                .build();
        Community savedCommunity = communityRepository.save(community);

        return savedCommunity.getCommunityId();
    }

    //게시글 상세 조회
    public CommunityRes.CommunityDetailRes getCommunityDetail(Long communityId) {
        Community community = communityRepository.findById(communityId). orElse(null);
        if (community == null){
            throw new ResponseException(ResponseCode.NO_SUCH_COMMUNITY);
        }
        else{
            List<String> imageUrlList = new ArrayList<>();
            community.getImages().forEach(image -> imageUrlList.add(awsS3Service.getUrlFromFileName(image)));
            return CommunityRes.CommunityDetailRes.of(community, imageUrlList);
        }
    }
}
