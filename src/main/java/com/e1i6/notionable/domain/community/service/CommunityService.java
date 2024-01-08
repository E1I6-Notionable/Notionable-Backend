package com.e1i6.notionable.domain.community.service;

import com.e1i6.notionable.domain.community.dto.community.CommunityReq;
import com.e1i6.notionable.domain.community.dto.community.CommunityRes;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CommunityService {
    CommunityRes.CommunityListRes getCommunity(String keyword, String filter, Pageable pageable);
    Long addCommunity(Long userId, List<MultipartFile> multipartFiles, CommunityReq communityReq);
    CommunityRes.CommunityDetailRes getCommunityDetail(Long communityId);
}
