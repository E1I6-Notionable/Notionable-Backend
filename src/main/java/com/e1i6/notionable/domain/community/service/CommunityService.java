package com.e1i6.notionable.domain.community.service;

import com.e1i6.notionable.domain.community.dto.community.CommunityDetailRes;
import com.e1i6.notionable.domain.community.dto.community.CommunityReq;
import com.e1i6.notionable.domain.community.dto.community.CommunityListRes;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CommunityService {
    List<CommunityListRes> getCommunity(String keyword, String filter, Pageable pageable);
    Long addCommunity(Long userId, List<MultipartFile> multipartFiles, CommunityReq communityReq);
    CommunityDetailRes getCommunityDetail(Long communityId);
}
