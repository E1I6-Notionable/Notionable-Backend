package com.e1i6.notionable.domain.community.service;

import com.e1i6.notionable.domain.community.dto.CommunityReq;
import com.e1i6.notionable.domain.community.dto.CommunityRes;
import com.e1i6.notionable.domain.community.entity.Community;
import com.e1i6.notionable.domain.community.repository.CommunityRepository;
import com.e1i6.notionable.domain.user.entity.User;
import com.e1i6.notionable.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommunityServiceImpl implements CommunityService{
    private final CommunityRepository communityRepository;
    private final UserRepository userRepository;
    public List<CommunityRes> getAllCommunity(String keyword, String filter, Pageable pageable) {
        Page<Community> allCommunity = communityRepository.findByKeywordAndFilter(keyword, filter, pageable);
        List<CommunityRes> communityResList = allCommunity.getContent().stream()
                .map(community -> new CommunityRes(
                        community.getCommunityId(),
                        community.getCategory(),
                        community.getCommunityLike(),
                        community.getTitle(),
                        community.getContent(),
                        community.getUser().getUserId(),
                        community.getCreatedAt()
                ))
                .collect(Collectors.toList());

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
