package com.e1i6.notionable.domain.community.service;

import com.e1i6.notionable.domain.community.dto.CommunityReq;
import com.e1i6.notionable.domain.community.dto.CommunityRes;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CommunityService {
    CommunityRes.CommunityListRes getCommunity(Long userId, String keyword, String filter, Pageable pageable);
    Long addCommunity(Long userId, List<MultipartFile> multipartFiles, CommunityReq communityReq);
    CommunityRes.CommunityDetailRes getCommunityDetail(Long userId, Long communityId);
    boolean likeCommunity(Long userId, Long communityId);
    List<CommunityRes.CommunityInfo> getTopCommunity(Long userId);
    CommunityRes.CommunityListRes getMyCommunity(Long userId, Pageable pageable);
    String deleteCommunity(Long userId, Long communityId);
}
