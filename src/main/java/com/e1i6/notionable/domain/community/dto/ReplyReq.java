package com.e1i6.notionable.domain.community.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

//대댓글 작성 형식
@Data
@Builder
@AllArgsConstructor
public class ReplyReq {
    private String content;

    public ReplyReq(){}
}
