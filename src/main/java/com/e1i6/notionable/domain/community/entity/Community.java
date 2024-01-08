package com.e1i6.notionable.domain.community.entity;

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
    private Long communityLike;
    private Long communityComment;
    private String title;
    private String content;
    private String thumbnail;

    @ElementCollection
    private List<String> images = new ArrayList<>();

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public void addComment() {
        this.communityComment += 1;
    }
}
