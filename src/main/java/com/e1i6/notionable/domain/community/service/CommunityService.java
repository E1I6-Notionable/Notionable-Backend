package com.e1i6.notionable.domain.community.service;

import com.e1i6.notionable.domain.community.dto.CommunityReq;
import com.e1i6.notionable.domain.community.dto.CommunityRes;
import com.e1i6.notionable.domain.community.dto.LikeRes;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CommunityService {
    CommunityRes.CommunityListRes getCommunity(Long userId, String keyword, String filter, Pageable pageable);
    Long addCommunity(Long userId, List<MultipartFile> multipartFiles, CommunityReq communityReq);
    CommunityRes.CommunityDetailRes getCommunityDetail(Long userId, Long communityId);
    LikeRes likeCommunity(Long userId, Long communityId);
}
