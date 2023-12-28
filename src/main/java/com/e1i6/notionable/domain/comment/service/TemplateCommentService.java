package com.e1i6.notionable.domain.comment.service;

import com.e1i6.notionable.domain.comment.data.TemplateCommentDto;
import com.e1i6.notionable.domain.comment.data.TemplateCommentReqDto;
import com.e1i6.notionable.domain.comment.entity.TemplateComment;
import com.e1i6.notionable.domain.comment.repository.CommentRepository;
import com.e1i6.notionable.global.common.response.ResponseCode;
import com.e1i6.notionable.global.common.response.ResponseException;
import com.e1i6.notionable.global.service.AwsS3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TemplateCommentService {

    private final CommentRepository commentRepository;
    private final AwsS3Service awsS3Service;

    public TemplateCommentDto createComment(
            Long userId,
            TemplateCommentReqDto reqDto,
            List<MultipartFile> multipartFiles) {

        List<String> uploadedUrls = awsS3Service.uploadFiles(multipartFiles);

        TemplateComment newComment = TemplateComment.builder()
                .userId(userId)
                .templateId(reqDto.getTemplateId())
                .content(reqDto.getContent())
                .rate(reqDto.getRate())
                .images(uploadedUrls)
                .build();

        commentRepository.save(newComment);

        return TemplateCommentDto.builder()
                .commentId(newComment.getCommentId())
                .rate(newComment.getRate())
                .content(newComment.getContent())
                .images(newComment.getImages())
                .build();
    }

    public String deleteComment(Long userId, Long commentId) {
        Optional<TemplateComment> optionalComment = commentRepository.findById(commentId);

        if (optionalComment.isPresent()) {
            if (optionalComment.get().getUserId() == userId) {
                commentRepository.delete(optionalComment.get());
                return "delete success";
            } else {
                throw new ResponseException(ResponseCode.NO_AUTHORIZATION);
            }
        } else {
            throw new ResponseException(ResponseCode.NO_SUCH_COMMENT);
        }
    }
}
