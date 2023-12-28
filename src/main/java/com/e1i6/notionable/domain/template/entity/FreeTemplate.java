package com.e1i6.notionable.domain.template.entity;

import com.e1i6.notionable.global.common.entity.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FreeTemplate extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long freeTemplateId;

    @NotNull
    private Long userId;

    private String title;

    @Lob
    private String content;

    private String tunmbnail;

    private String category;

    private String notionUrl;

    @ElementCollection
    private List<String> images = new ArrayList<>();
}
