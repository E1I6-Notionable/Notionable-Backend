package com.e1i6.notionable.domain.comment.service;

import com.e1i6.notionable.domain.comment.data.TemplateCommentDto;
import com.e1i6.notionable.domain.comment.data.TemplateCommentReqDto;
import com.e1i6.notionable.domain.comment.entity.TemplateComment;
import com.e1i6.notionable.domain.comment.repository.CommentRepository;
import com.e1i6.notionable.domain.user.entity.User;
import com.e1i6.notionable.domain.user.repository.UserRepository;
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
    private final UserRepository userRepository;
    private final AwsS3Service awsS3Service;

    public TemplateCommentDto createComment(
            Long userId,
            Long templateId,
            TemplateCommentReqDto reqDto,
            List<MultipartFile> multipartFiles) {

        List<String> uploadedUrls = awsS3Service.uploadFiles(multipartFiles);

        TemplateComment newComment = TemplateComment.builder()
                .userId(userId)
                .templateId(templateId)
                .content(reqDto.getContent())
                .rate(reqDto.getRate())
                .images(uploadedUrls)
                .build();

        commentRepository.save(newComment);
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User curUser = optionalUser.get();

            return TemplateCommentDto.builder()
                    .commentId(newComment.getCommentId())
                    .nickName(curUser.getNickName())
                    .profile(curUser.getProfile())
                    .rate(newComment.getRate())
                    .content(newComment.getContent())
                    .images(newComment.getImages())
                    .build();
        } else {
            throw new ResponseException(ResponseCode.NO_SUCH_USER);
        }
    }

    public TemplateCommentDto updateComment(
            Long userId,
            Long commentId,
            TemplateCommentDto reqDto,
            List<MultipartFile> multipartFiles) {

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
