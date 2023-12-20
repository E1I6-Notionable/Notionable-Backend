package com.e1i6.notionable.domain.template.data.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class FreeTemplateResDto {
    private String title;
    private String content;
    private String thumbnail;
    private List<String> images;
}
