package com.e1i6.notionable.domain.answer.dto;

import com.e1i6.notionable.domain.answer.entity.Answer;
import com.e1i6.notionable.domain.creator.entity.Creator;
import com.e1i6.notionable.domain.inquiry.dto.InquiryDto;
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

    public static AnswerDto toAnswerDto(Answer answer) {
        AnswerDto answerDto = new AnswerDto();

        answerDto.setAnswer_id(answer.getAnswer_id());
        answerDto.setTitle(answer.getTitle());
        answerDto.setContent(answer.getContent());
        answerDto.setCreatedAt(answer.getCreatedAt());
        answerDto.setStatus(answer.getStatus());
        answerDto.setFileUrl(answer.getFileUrl());
        answerDto.setInquiry(answer.getInquiry());
        answerDto.setCreator(answer.getCreator());

        return answerDto;
    }
}
