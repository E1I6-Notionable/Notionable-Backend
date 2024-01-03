package com.e1i6.notionable.domain.review.service;

import com.e1i6.notionable.domain.review.data.ReviewDto;
import com.e1i6.notionable.domain.review.data.ReviewUpdateReqDto;
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

        Review newReview;
        if (multipartFiles == null) {
            newReview = Review.builder()
                    .template(template)
                    .user(user)
                    .content(reqDto.getContent())
                    .rate(reqDto.getRate())
                    .images(null)
                    .build();
        }
        else {
            List<String> uploadedFiles = awsS3Service.uploadFiles(multipartFiles);

            newReview = Review.builder()
                    .template(template)
                    .user(user)
                    .content(reqDto.getContent())
                    .rate(reqDto.getRate())
                    .images(uploadedFiles)
                    .build();
        }

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
            ReviewUpdateReqDto reqDto,
            List<MultipartFile> multipartFiles) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResponseException(ResponseCode.NO_SUCH_REVIEW));
        User user = userRepository.findById(review.getUser().getUserId())
                .orElseThrow(() -> new ResponseException(ResponseCode.NO_SUCH_USER));

        if (userId != user.getUserId()) {
            throw new ResponseException(ResponseCode.NO_AUTHORIZATION);
        }

        List<String> newImages = new ArrayList<>();
        // 기존에 올렸던 파일 모두 재업로드 안 한 경우
        if (reqDto.getImageUrls().isEmpty()) {
            // s3에 업로드된 파일 삭제
            review.getImages().forEach(awsS3Service::deleteFile);

            // 추가로 업로드된 파일을 저장
            if (multipartFiles != null) {
                List<String> uploadedFileNames = awsS3Service.uploadFiles(multipartFiles);
                review.updateReview(new ReviewUpdateDto(reqDto, uploadedFileNames));
            }
            // 추가로 업로드된 파일도 없다면 null 저장
            else
                review.updateReview(new ReviewUpdateDto(reqDto, null));
        }
        // 기존에 올렸던 파일 전부 또는 일부를 재업로드 한 경우
        else {
            // 재업로드된 파일을 찾아 리스트에 추가, 없다면 삭제
            List<String> beforeImages = review.getImages();
            reqDto.getImageUrls().forEach(imageUrl -> {
                String fileName = awsS3Service.getFileNameFromUrl(imageUrl);

                if (beforeImages.contains(fileName)) {
                    log.info("added file: {}", fileName);
                    newImages.add(fileName);
                }
                else {
                    awsS3Service.deleteFile(fileName);
                    log.info("deleted file: {}", fileName);
                }
            });

            // 추가로 업로드한 파일이 존재
            if (multipartFiles == null) {
                review.updateReview(new ReviewUpdateDto(reqDto, newImages));
            }
            // 추가로 업로드한 파일이 존재하지 않음
            else {
                List<String> uploadedFileNames = awsS3Service.uploadFiles(multipartFiles);
                newImages.addAll(uploadedFileNames);
                review.updateReview(new ReviewUpdateDto(reqDto, newImages));
            }
        }

        reviewRepository.save(review);
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
