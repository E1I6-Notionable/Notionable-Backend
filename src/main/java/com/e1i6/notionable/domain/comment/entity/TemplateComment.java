package com.e1i6.notionable.domain.comment.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TemplateComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    private Long templateId;

    private Long userId;

    private String rate;

    private String content;

    @ElementCollection
    private List<String> images = new ArrayList<>();
}
