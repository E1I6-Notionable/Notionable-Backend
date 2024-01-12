package com.e1i6.notionable.domain.inquiry.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "INQUIRY")
public class Inquiry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inquiry_id")
    private Long inquiry_id;
    private String title;
    private String content;
    private LocalDateTime createdAt; // 문의 작성일
    private String status; // 답변유무 (Yes, No)
}
