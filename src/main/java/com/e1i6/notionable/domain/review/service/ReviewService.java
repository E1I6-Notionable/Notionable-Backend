package com.e1i6.notionable.domain.review.service;

import com.e1i6.notionable.domain.review.data.ReviewDto;
import com.e1i6.notionable.domain.review.data.ReviewReqDto;
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
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final TemplateRepository templateRepository;
    private final AwsS3Service awsS3Service;

    public String createComment(
            Long userId,
            ReviewReqDto reqDto,
            List<MultipartFile> multipartFiles) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseException(ResponseCode.NO_SUCH_USER));
        Template template = templateRepository.findById(reqDto.getTemplateId())
                .orElseThrow(() -> new ResponseException(ResponseCode.NO_SUCH_TEMPLATE));

        List<String> uploadedFiles = awsS3Service.uploadFiles(multipartFiles);

        Review newComment = Review.builder()
                .template(template)
                .user(user)
                .content(reqDto.getContent())
                .rate(reqDto.getRate())
                .images(uploadedFiles)
                .build();

        reviewRepository.save(newComment);

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

    public String updateReview(
            Long userId,
            Long reviewId,
            ReviewReqDto reqDto,
            List<MultipartFile> multipartFiles) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResponseException(ResponseCode.NO_SUCH_REVIEW));
        User user = userRepository.findById(review.getUser().getUserId())
                .orElseThrow(() -> new ResponseException(ResponseCode.NO_SUCH_USER));

        if (userId != user.getUserId()) {
            throw new ResponseException(ResponseCode.NO_AUTHORIZATION);
        }

        return null;
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
