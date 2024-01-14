package com.e1i6.notionable.domain.inquiryanswer.answer.dto;

import com.e1i6.notionable.domain.inquiryanswer.answer.entity.Answer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnswerDto {

    private Long answer_id;
    private String title;
    private String content;
    private String fileUrl;
    private LocalDateTime createdAt; // 답변 작성일
    private String status; // 답변유무 (Yes, No)
    private Long creator_id;
    private Long template_id;
    private Long inquiry_id;

    public static AnswerDto toAnswerDto(Answer answer) {
        AnswerDto answerDto = new AnswerDto();

        answerDto.setAnswer_id(answer.getAnswer_id());
        answerDto.setTitle(answer.getTitle());
        answerDto.setContent(answer.getContent());
        answerDto.setCreatedAt(answer.getCreatedAt());
        answerDto.setStatus(answer.getStatus());
        answerDto.setFileUrl(answer.getFileUrl());
        answerDto.setInquiry_id(answer.getInquiry().getInquiry_id());
        answerDto.setTemplate_id(answer.getTemplate_id());
        answerDto.setCreator_id(answer.getCreator().getCreator_id());

        return answerDto;
    }
}
