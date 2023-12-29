package com.e1i6.notionable.domain.comment.data;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class TemplateCommentReqDto {
    private String rate;
    private String content;
}
