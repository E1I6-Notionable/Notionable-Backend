package com.e1i6.notionable.domain.review.controller;

import com.e1i6.notionable.domain.review.data.ReviewDto;
import com.e1i6.notionable.domain.review.data.ReviewUpdateReqDto;
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
            @RequestPart(value = "files", required = false) List<MultipartFile> multipartFiles) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(authentication.getName());

        try {
            return new BaseResponse<>(reviewService.createReview(userId, reqDto, multipartFiles));
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

    @GetMapping("/list")
    public BaseResponse<List<ReviewDto>> getMyReview() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(authentication.getName());

        try {
            return new BaseResponse<>(reviewService.getMyReview(userId));
        } catch (ResponseException e) {
            return new BaseResponse<>(e.getErrorCode(), e.getMessage());
        } catch (Exception e) {
            return new BaseResponse<>(ResponseCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }


    @PutMapping("/{reviewId}")
    public BaseResponse<String> updateReview(
            @PathVariable Long reviewId,
            @RequestPart ReviewUpdateReqDto reqDto,
            @RequestPart(value = "files", required = false) List<MultipartFile> multipartFiles) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(authentication.getName());

        try {
            return new BaseResponse<>(reviewService.updateReview(userId, reviewId, reqDto, multipartFiles));
        } catch (ResponseException e) {
            return new BaseResponse<>(e.getErrorCode(), e.getMessage());
        } catch (Exception e) {
            return new BaseResponse<>(ResponseCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @DeleteMapping("/{reviewId}")
    public BaseResponse<String> deleteReview(@PathVariable Long reviewId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(authentication.getName());

        try {
            String result = reviewService.deleteReview(userId, reviewId);
            return new BaseResponse<>(result);
        } catch (ResponseException e) {
            return new BaseResponse<>(e.getErrorCode(), e.getMessage());
        } catch (Exception e) {
            return new BaseResponse<>(ResponseCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}