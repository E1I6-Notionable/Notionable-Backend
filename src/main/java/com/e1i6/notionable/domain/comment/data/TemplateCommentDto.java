package com.e1i6.notionable.domain.comment.data;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class TemplateCommentDto {
    private Long commentId;
    private String nickName;
    private String profile;
    private String rate;
    private String content;
    private List<String> images;
}
