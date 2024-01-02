package com.e1i6.notionable.domain.review.service;

import com.e1i6.notionable.domain.review.data.ReviewDto;
import com.e1i6.notionable.domain.review.data.ReviewUploadReqDto;
import com.e1i6.notionable.domain.review.data.ReviewUpdateDto;
import com.e1i6.notionable.domain.review.entity.Review;
import com.e1i6.notionable.domain.review.repository.ReviewRepository;
import com.e1i6.notionable.domain.template.entity.Template;
import com.e1i6.notionable.domain.template.repository.TemplateRepository;
import com.e1i6.notionable.domain.user.entity.User;
import com.e1i6.notionable.domain.user.repository.UserRepository;
import com.e1i6.notionable.global.common.response.ResponseCode;
import com.e1i6.notionable.global.common.response.ResponseException;
import com.e1i6.notionable.global.service.AwsS3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final TemplateRepository templateRepository;
    private final AwsS3Service awsS3Service;

    public String createReview(
            Long userId,
            ReviewUploadReqDto reqDto,
            List<MultipartFile> multipartFiles) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseException(ResponseCode.NO_SUCH_USER));
        Template template = templateRepository.findById(reqDto.getTemplateId())
                .orElseThrow(() -> new ResponseException(ResponseCode.NO_SUCH_TEMPLATE));

        List<String> uploadedFiles = awsS3Service.uploadFiles(multipartFiles);

        Review newReview = Review.builder()
                .template(template)
                .user(user)
                .content(reqDto.getContent())
                .rate(reqDto.getRate())
                .images(uploadedFiles)
                .build();

        reviewRepository.save(newReview);

        return "review create success";
    }

    public List<ReviewDto> getReviews(Long templateId) {
        Template template = templateRepository.findById(templateId)
                .orElseThrow(() -> new ResponseException(ResponseCode.NO_SUCH_TEMPLATE));

        List<Review> reviewList = reviewRepository.findByTemplate(template);
        List<ReviewDto> reviewDtoList = new ArrayList<>();
        reviewList.forEach(review -> {
           List<String> imageUrls = new ArrayList<>();
           review.getImages().forEach(image -> imageUrls.add(awsS3Service.getUrlFromFileName(image)));
           reviewDtoList.add(Review.toReviewDto(review, review.getUser(), imageUrls));
        });

        return reviewDtoList;
    }

    @Transactional
    public String updateReview(
            Long userId,
            Long reviewId,
            ReviewUploadReqDto reqDto,
            List<MultipartFile> multipartFiles) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResponseException(ResponseCode.NO_SUCH_REVIEW));
        User user = userRepository.findById(review.getUser().getUserId())
                .orElseThrow(() -> new ResponseException(ResponseCode.NO_SUCH_USER));

        if (userId != user.getUserId()) {
            throw new ResponseException(ResponseCode.NO_AUTHORIZATION);
        }

        review.getImages().forEach(awsS3Service::deleteFile);
        List<String> uploadedFiles = awsS3Service.uploadFiles(multipartFiles);
        review.updateReview(new ReviewUpdateDto(reqDto, uploadedFiles));

        return "update review success";
    }

    @Transactional
    public String deleteReview(Long userId, Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResponseException(ResponseCode.NO_SUCH_REVIEW));
        User user = userRepository.findById(review.getUser().getUserId())
                .orElseThrow(() -> new ResponseException(ResponseCode.NO_SUCH_USER));

        if (userId != user.getUserId()) {
            throw new ResponseException(ResponseCode.NO_AUTHORIZATION);
        }

        review.getImages().forEach(awsS3Service::deleteFile);

        reviewRepository.delete(review);
        return "review delete success";
    }
}
