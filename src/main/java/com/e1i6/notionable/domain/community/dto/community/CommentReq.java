package com.e1i6.notionable.domain.community.dto.community;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

//댓글 작성 형식
@Data
@Builder
@AllArgsConstructor
public class CommentReq {
    private String content;

    public CommentReq() {
    }
}
