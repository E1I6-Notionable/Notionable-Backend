package com.e1i6.notionable.domain.answer.dto;

import com.e1i6.notionable.domain.creator.entity.Creator;
import com.e1i6.notionable.domain.inquiry.entity.Inquiry;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnswerDto {

    private Long answer_id;
    private String title;
    private String content;
    private LocalDateTime createdAt; // 답변 작성일
    private String status; // 답변유무 (Yes, No)
    private String fileUrl;
    private Inquiry inquiry;
    private Creator creator;
}
