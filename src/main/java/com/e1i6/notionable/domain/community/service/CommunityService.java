package com.e1i6.notionable.domain.community.service;

import com.e1i6.notionable.domain.community.dto.CommunityReq;
import com.e1i6.notionable.domain.community.entity.Community;
import com.e1i6.notionable.domain.community.repository.CommunityRepository;
import com.e1i6.notionable.domain.user.entity.User;
import com.e1i6.notionable.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommunityService {
    private final CommunityRepository communityRepository;
    private final UserRepository userRepository;
//    public List<CommunityRes> getAllCommunity(String keyword, String filter, Pageable pageable) {
//        Page<Community> allCommunity = communityRepository.findByKeyword(keyword, pageable);
//        List<CommunityRes> communityResList = allCommunity
//                .stream()
//                .map(community -> new CommunityRes(
//                        community.getCommunity_id(),
//                        community.getCategory(),
//                        community.getLike(),
//                        community.getTitle(),
//                        community.getContent(),
//                        community.getUser()
//                ))
//                .collect(Collectors.toList());
//
//        return communityResList;
//    }

    public List<Community> getAllCommunity() {
        List<Community> communityResList = communityRepository.findAll();

        return communityResList;
    }

    public CommunityReq addCommunityInformation(Long userId, CommunityReq communityReq) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        Community community = Community.builder()
                .category(communityReq.getCategory())
                .communityLike(0L)
                .title(communityReq.getTitle())
                .content(communityReq.getContent())
                .user(user)
                .build();

        Community savedCommunity = communityRepository.save(community);

        return CommunityReq.mapCommunityReq(savedCommunity);
    }
}
