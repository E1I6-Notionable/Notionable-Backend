package com.e1i6.notionable.domain.inquiryanswer.inquiry.entity;

import com.e1i6.notionable.domain.inquiryanswer.answer.entity.Answer;
import com.e1i6.notionable.domain.user.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private String fileUrl;
    private LocalDateTime createdAt; // 문의 작성일
    private Long template_id;
    private String status; // 답변유무 (Yes, No)

//    @ManyToOne
//    @JoinColumn(name = "template_id")
//    @JsonIgnore
//    private Template template;

    // 여러 개의 문의가 하나의 유저에게 속할 수 있으므로 N:1
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    // 하나의 문의에는 하나의 답변만 달릴 수 있으므로 1:1
    @OneToOne(mappedBy = "inquiry", cascade = CascadeType.ALL)
    @JsonIgnore
    private Answer answer;

    public void modifyInquiryStatus(String status) {
        this.status = status;
    }
}
