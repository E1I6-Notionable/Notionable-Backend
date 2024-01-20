package com.e1i6.notionable.domain.community.entity;

import com.e1i6.notionable.domain.community.dto.CommentReq;
import com.e1i6.notionable.domain.community.dto.CommunityReq;
import com.e1i6.notionable.domain.user.entity.User;
import com.e1i6.notionable.global.common.entity.BaseTimeEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Community extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "community_id")
    private Long communityId;

    private String category;
    private String title;
    private String content;
    private String thumbnail;

    @ElementCollection
    private List<String> images = new ArrayList<>();

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @JsonIgnore
    @OneToMany(mappedBy = "community", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommunityComment> comments = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "community", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommunityLike> likes = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "community", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommunityReply> replies = new ArrayList<>();

    public void updateCommunity(CommunityReq communityReq, String thumbnail, List<String> images){
        this.content = communityReq.getContent();
        this.title = communityReq.getTitle();
        this.category = communityReq.getCategory();
        this.thumbnail = thumbnail;
        this.images = images;

    }

}
