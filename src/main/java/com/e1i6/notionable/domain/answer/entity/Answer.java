package com.e1i6.notionable.domain.answer.entity;

import com.e1i6.notionable.domain.creator.entity.Creator;
import com.e1i6.notionable.domain.inquiry.entity.Inquiry;
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
@Table(name = "ANSWER")
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "answer_id")
    private Long answer_id;
    private String title;
    private String content;
    private LocalDateTime createdAt; // 답변 작성일
    private String status; // 답변유무 (Yes, No)
    private String fileUrl;

    // Answer과 Inquiry의 1:1 관계
    @OneToOne
    @JoinColumn(name = "inquiry_id")
    private Inquiry inquiry;

    // Answer과 Creator의 N:1 관계
    @ManyToOne
    @JoinColumn(name = "creator_id")
    private Creator creator;
}
