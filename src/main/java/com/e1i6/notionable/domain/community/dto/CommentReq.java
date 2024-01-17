package com.e1i6.notionable.domain.community.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//댓글 작성 형식
@Data
public class CommentReq {
    private String content;
}
