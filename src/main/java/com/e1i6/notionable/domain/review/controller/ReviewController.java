package com.e1i6.notionable.domain.review.controller;

import com.e1i6.notionable.domain.review.data.ReviewDto;
import com.e1i6.notionable.domain.review.data.ReviewUploadReqDto;
import com.e1i6.notionable.domain.review.service.ReviewService;
import com.e1i6.notionable.global.common.response.BaseResponse;
import com.e1i6.notionable.global.common.response.ResponseCode;
import com.e1i6.notionable.global.common.response.ResponseException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/template/review")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("")
    public BaseResponse<String> createReview(
            @RequestPart ReviewUploadReqDto reqDto,
            @RequestPart("files") List<MultipartFile> multipartFiles) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(authentication.getName());

        try {
            return new BaseResponse<>(reviewService.createComment(userId, reqDto, multipartFiles));
        } catch (ResponseException e) {
            return new BaseResponse<>(e.getErrorCode(), e.getMessage());
        } catch (Exception e) {
            return new BaseResponse<>(ResponseCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping("/list/{templateId}")
    public BaseResponse<List<ReviewDto>> getReviews(@PathVariable Long templateId) {
        try {
            return new BaseResponse<>(reviewService.getReviews(templateId));
        } catch (ResponseException e) {
            return new BaseResponse<>(e.getErrorCode(), e.getMessage());
        } catch (Exception e) {
            return new BaseResponse<>(ResponseCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PutMapping("/{commentId}")
    public BaseResponse<String> updateReview(
            @PathVariable Long commentId,
            @RequestPart ReviewUploadReqDto reqDto,
            @RequestPart("files") List<MultipartFile> multipartFiles) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(authentication.getName());

        try {
            return new BaseResponse<>(reviewService.updateReview(userId, commentId, reqDto, multipartFiles));
        } catch (ResponseException e) {
            return new BaseResponse<>(e.getErrorCode(), e.getMessage());
        } catch (Exception e) {
            return new BaseResponse<>(ResponseCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @DeleteMapping("/{commentId}")
    public BaseResponse<String> deleteReview(@PathVariable Long commentId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(authentication.getName());

        try {
            String result = reviewService.deleteReview(userId, commentId);
            return new BaseResponse<>(result);
        } catch (ResponseException e) {
            return new BaseResponse<>(e.getErrorCode(), e.getMessage());
        } catch (Exception e) {
            return new BaseResponse<>(ResponseCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}