package com.e1i6.notionable.domain.user.service;

import com.e1i6.notionable.domain.user.data.dto.request.SocialLoginReqDto;
import com.e1i6.notionable.domain.user.data.dto.response.SocialLoginResDto;
import com.e1i6.notionable.domain.user.entity.User;
import com.e1i6.notionable.domain.user.repository.UserRepository;
import com.e1i6.notionable.global.auth.JwtProvider;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class SociaLoginServiceImpl implements SocialLoginService{
    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    //Kakao
    @Value("${oauth.kakao.client-id}")
    private String KAKAO_CLIENT_ID;
    @Value("${oauth.kakao.redirect-uri}")
    private String KAKAO_REDIRECT_URI;
    @Value("${oauth.kakao.resource-uri}")
    private String KAKAO_RESOURCE_URI;
    @Value("${oauth.kakao.user-resource-uri}")
    private String KAKAO_USER_INFO;
    //Naver
    @Value("${oauth.naver.client-id}")
    private String NAVER_CLIENT_ID;
    @Value("${oauth.naver.client-secret}")
    private String NAVER_CLIENT_SECRET;
    @Value("${oauth.naver.redirect-uri}")
    private String NAVER_REDIRECT_URI;
    @Value("${oauth.naver.resource-uri}")
    private String NAVER_RESOURCE_URI;
    @Value("${oauth.naver.user-resource-uri}")
    private String NAVER_USER_INFO;

    private String TYPE;
    private String CLIENT_ID;
    private String REDIRECT_URI;
    private String RESOURCE_URI;
    private String USER_INFO;

    public SocialLoginResDto socialLogin(String code, String type) throws JsonProcessingException{
        TYPE = type;

        if(type == "kakao"){
            CLIENT_ID = KAKAO_CLIENT_ID;
            REDIRECT_URI = KAKAO_REDIRECT_URI;
            RESOURCE_URI = KAKAO_RESOURCE_URI;
            USER_INFO = KAKAO_USER_INFO;

        }
        else{
            CLIENT_ID = NAVER_CLIENT_ID;
            REDIRECT_URI = NAVER_REDIRECT_URI;
            RESOURCE_URI = NAVER_RESOURCE_URI;
            USER_INFO = NAVER_USER_INFO;
        }

        String accessTokenResponse = getAccessTokenResponse(code);
        JsonNode userInfoResponse = getUserInfoByAccessTokenResponse(accessTokenResponse);
        if(type == "kakao"){
            return kakaoLogin(userInfoResponse);
        }
        else{ //naver
            return NaverLogin(userInfoResponse);
        }

    }

    private String getAccessTokenResponse(String code){
        // HttpHeader object 생성
        HttpHeaders headers = new HttpHeaders();;
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HttpHeader와 HttpBody를 하나의 obejct에 담기
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", CLIENT_ID);
        if (TYPE =="naver"){
            params.add("client_secret", NAVER_CLIENT_SECRET);
        }
        params.add("redirect_uri", REDIRECT_URI);
        params.add("code", code);

        HttpEntity<MultiValueMap<String, String>> accessTokenRequest = new HttpEntity<>(params, headers);

        RestTemplate restTemplate = new RestTemplate();
        String responseBody = restTemplate.exchange(
                RESOURCE_URI,
                HttpMethod.POST,
                accessTokenRequest,
               String.class
        ).getBody();

        //액세스 토큰 파싱
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(responseBody);
            return jsonNode.get("access_token").asText();
        } catch (Exception e){
            log.info("in exception");
            return e.toString();
        }
    }

    public JsonNode getUserInfoByAccessTokenResponse(String accessToken) throws JsonProcessingException {

        log.info("accesstoken = {}", accessToken);
        // HttpHeader object 생성
        HttpHeaders headers = new HttpHeaders();;
        headers.add("Authorization", "Bearer "+accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String, String>> userInfoRequest = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.exchange(USER_INFO, HttpMethod.POST, userInfoRequest, JsonNode.class).getBody(); // 유저 정보를 json으로 가져옴.

    }

    public SocialLoginResDto kakaoLogin(JsonNode userResourceNode) throws JsonProcessingException {
        log.info("userResorceNode = {}", userResourceNode);

        String email = userResourceNode.get("kakao_account").get("email").asText();
        String nickName = userResourceNode.get("properties").get("nickname").asText();
        String is_email_verified = userResourceNode.get("kakao_account").get("is_email_verified").asText();
        String profile_image = userResourceNode.get("properties").get("profile_image").asText();

        // 카카오 서버 상에서 검증된 이메일인 경우
        if(is_email_verified.equals("true")){
            Optional<User> userByKakao= userRepository.findUserByEmail(email);

            if(userByKakao .isPresent()){
                User user = userByKakao.get();
                return SocialLoginResDto.builder()
                        .jwtDto(jwtProvider.generateToken(user.getUserId()))
                        .email(user.getEmail())
                        .nickName(user.getNickName())
                        .phoneNumber(user.getPhoneNumber())
                        .profile(user.getProfile())
                        .build();
            } else{
                SocialLoginReqDto newKakaoUserDto = SocialLoginReqDto.builder()
                        .email(email)
                        .password(bCryptPasswordEncoder.encode(UUID.randomUUID().toString()))
                        .userType(2)
                        .phoneNumber(profile_image)
                        .nickName(nickName)
                        .build();
                User newKakaouser = new User(newKakaoUserDto);
                userRepository.save(newKakaouser);
                return SocialLoginResDto.builder()
                        .jwtDto(jwtProvider.generateToken(newKakaouser.getUserId()))
                        .email(newKakaouser.getEmail())
                        .nickName(newKakaouser.getNickName())
                        .phoneNumber(newKakaouser.getPhoneNumber())
                        .profile(newKakaouser.getProfile())
                        .build();
            }
        }
        return null;
    }

    public SocialLoginResDto NaverLogin(JsonNode userResourceNode) throws JsonProcessingException {
        log.info("userResorceNode = {}", userResourceNode);

        String email = userResourceNode.get("response").get("email").asText();
        String nickName = userResourceNode.get("response").get("nickname").asText();
        String phoneNumber = userResourceNode.get("response").get("mobile").asText();
        String profile_image = userResourceNode.get("response").get("profile_image").asText();

        Optional<User> userByNaver= userRepository.findUserByEmail(email);
        if(userByNaver .isPresent()){
            User user = userByNaver.get();
            return SocialLoginResDto.builder()
                    .jwtDto(jwtProvider.generateToken(user.getUserId()))
                    .email(user.getEmail())
                    .nickName(user.getNickName())
                    .phoneNumber(user.getPhoneNumber())
                    .profile(user.getProfile())
                    .build();
        } else{
            SocialLoginReqDto newNaverUserDto = SocialLoginReqDto.builder()
                    .email(email)
                    .password(bCryptPasswordEncoder.encode(UUID.randomUUID().toString()))
                    .userType(3)
                    .phoneNumber(phoneNumber)
                    .profile(profile_image)
                    .nickName(nickName)
                    .build();
            User newNaveruser = new User(newNaverUserDto);
            userRepository.save(newNaveruser);
            return SocialLoginResDto.builder()
                    .jwtDto(jwtProvider.generateToken(newNaveruser.getUserId()))
                    .email(newNaveruser.getEmail())
                    .nickName(newNaveruser.getNickName())
                    .phoneNumber(newNaveruser.getPhoneNumber())
                    .profile(newNaveruser.getProfile())
                    .build();
        }
    }


}

