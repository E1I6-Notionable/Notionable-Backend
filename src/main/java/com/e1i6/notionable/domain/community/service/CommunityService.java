package com.e1i6.notionable.domain.community.service;

import com.e1i6.notionable.domain.community.dto.CommunityReq;
import com.e1i6.notionable.domain.community.dto.CommunityRes;
import com.e1i6.notionable.domain.community.entity.Community;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CommunityService {
    List<CommunityRes> getAllCommunity(String keyword, String filter, Pageable pageable);
    CommunityReq addCommunityInformation(Long userId, CommunityReq communityReq);
}
