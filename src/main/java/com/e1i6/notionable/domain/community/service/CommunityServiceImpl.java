package com.e1i6.notionable.domain.community.service;

import com.e1i6.notionable.domain.community.dto.CommunityReq;
import com.e1i6.notionable.domain.community.dto.CommunityRes;
import com.e1i6.notionable.domain.community.entity.Community;
import com.e1i6.notionable.domain.community.entity.CommunityLike;
import com.e1i6.notionable.domain.community.entity.CommunityReply;
import com.e1i6.notionable.domain.community.repository.CommentRepository;
import com.e1i6.notionable.domain.community.repository.CommunityRepository;
import com.e1i6.notionable.domain.community.repository.LikeRepository;
import com.e1i6.notionable.domain.community.repository.ReplyRepository;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommunityServiceImpl implements CommunityService{
    private final CommunityRepository communityRepository;
    private final AwsS3Service awsS3Service;
    private final UserRepository userRepository;
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;
    private final ReplyRepository replyRepository;

    public User findUser(Long userId){
        if (userId == null) {
            throw new ResponseException(ResponseCode.INVALID_USER);
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseException(ResponseCode.NO_SUCH_USER));
        return user;
    }

    public Community findCommunity(Long communityId){
        Community community = communityRepository.findById(communityId)
                .orElseThrow(() -> new ResponseException(ResponseCode.NO_SUCH_COMMUNITY));
        return community;
    }


    //게시글 목록 조회
    public CommunityRes.CommunityListRes getCommunity(Long userId, String keyword, String filter, Pageable pageable) {
        Page<Community> allCommunity = communityRepository.findByKeywordAndFilter(keyword, filter, pageable);
        List<CommunityRes.CommunityInfo> communityList = allCommunity.getContent().stream()
                .map(community -> CommunityRes.CommunityInfo.of(community,
                        getCommunityInfo(userId, community)))
                .collect(Collectors.toList());
        return CommunityRes.CommunityListRes.of(allCommunity, communityList);
    }

    //게시글 작성
    public Long addCommunity(Long userId, List<MultipartFile> multipartFiles, CommunityReq communityReq) {

        User user = findUser(userId);

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
                .title(communityReq.getTitle())
                .content(communityReq.getContent())
                .user(user)
                .build();
        Community savedCommunity = communityRepository.save(community);

        return savedCommunity.getCommunityId();
    }

    //게시글 상세 조회
    public CommunityRes.CommunityDetailRes getCommunityDetail(Long userId, Long communityId) {

        Community community = findCommunity(communityId);

        List<String> imageUrlList = new ArrayList<>();
        community.getImages().forEach(image -> imageUrlList.add(awsS3Service.getUrlFromFileName(image)));

        return CommunityRes.CommunityDetailRes.of(community, imageUrlList, getCommunityInfo(userId, community));
    }

    //게시물 좋아요
    @Transactional
    public boolean likeCommunity(Long userId, Long communityId) {

        User user = findUser(userId);
        Community community = findCommunity(communityId);

        boolean existLike = likeRepository.existsByUserAndCommunity(user, community);
        if(existLike){
            likeRepository.deleteByUserAndCommunity(user, community);
        }
        else{
            CommunityLike newLike = CommunityLike.builder()
                    .community(community)
                    .user(user)
                    .build();
            likeRepository.save(newLike);
        }

        return !existLike;
    }

    //5일이내 게시글 중 좋아요 수 top5
    public List<CommunityRes.CommunityInfo> getTopCommunity(Long userId) {
        List<Community> topCommunity = communityRepository.findTop5CommunitiesWithLikes();
        return topCommunity.stream()
                .map(community -> CommunityRes.CommunityInfo.of(community,
                        getCommunityInfo(userId, community)))
                .limit(5)
                .collect(Collectors.toList());
    }

    // 마이페이지 - 내가 쓴 글
    public CommunityRes.CommunityListRes getMyCommunity(Long userId, Pageable pageable) {
        User user = findUser(userId);
        Page<Community> allMyCommunity = communityRepository.findByUser(user, pageable);
        List<CommunityRes.CommunityInfo> communityList = allMyCommunity.getContent().stream()
                .map(community -> CommunityRes.CommunityInfo.of(community,
                        getCommunityInfo(userId, community)))
                .collect(Collectors.toList());
        return CommunityRes.CommunityListRes.of(allMyCommunity, communityList);
    }

    //좋아요 개수, 댓글 개수, 유저의 좋아요 유무
    public CommunityRes.CommunityGetInfo getCommunityInfo(Long userId, Community community){
        User user = userId != null ? userRepository.findById(userId).orElse(null) : null;
        return CommunityRes.CommunityGetInfo.builder()
                .communityLike(likeRepository.countByCommunity(community))
                .communityComment(commentRepository.countByCommunity(community) + replyRepository.countByCommunity(community))
                .existLike(likeRepository.existsByUserAndCommunity(user, community))
                .build();
    }

    //커뮤니티 삭제
    @Transactional
    public String deleteCommunity(Long userId, Long communityId){
        User user = findUser(userId);
        Community community = findCommunity(communityId);

        if (!user.equals(community.getUser())) {
            throw new ResponseException(ResponseCode.NO_AUTHORITY);
        }

        communityRepository.delete(community);
        return "커뮤니티가 삭제되었습니다.";
    }

    //게시글 수정
    public String modifyCommunity(Long userId, Long communityId, List<MultipartFile> multipartFiles, CommunityReq communityReq) {

        User user = findUser(userId);
        Community community = findCommunity(communityId);

        if (!user.equals(community.getUser())) {
            throw new ResponseException(ResponseCode.NO_AUTHORITY);
        }

        List<String> uploadedFileNames = new ArrayList<>();
        String thumbnailUrl = null;

        community.getImages().forEach(awsS3Service::deleteFile);
        if (multipartFiles != null && !multipartFiles.isEmpty()) {
            uploadedFileNames = awsS3Service.uploadFiles(multipartFiles);
            thumbnailUrl = awsS3Service.getUrlFromFileName(uploadedFileNames.get(0));
        }

        community.updateCommunity(communityReq , thumbnailUrl, uploadedFileNames);
        communityRepository.save(community);

        return "커뮤니티가 수정되었습니다.";
    }
}
