package com.e1i6.notionable.domain.user.service;

import com.e1i6.notionable.domain.user.data.dto.KakaoLoginDto;
import com.e1i6.notionable.domain.user.data.dto.KakaoOAuthToken;
import com.e1i6.notionable.domain.user.entity.User;
import com.e1i6.notionable.domain.user.repository.UserRepository;
import com.e1i6.notionable.global.auth.JwtProvider;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class SocialLoginServiceImpl implements SocialLoginService{
    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;
    private final Environment env;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    // 액세스 토큰 요청
    public JsonNode getAccessTokenResponse(String code) {

        RestTemplate restTemplate = new RestTemplate();

        // HttpHeader object 생성
        HttpHeaders headers = new HttpHeaders();;
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HttpBody object 생성
        String clientId = env.getProperty("oauth.kakao.client-id");
        String redirectUri = env.getProperty("oauth.kakao.redirect-uri");
        String resourceUri = env.getProperty("oauth.kakao.resource-uri");

        // 확인을 위한 log
        log.info("clientId = {}", clientId);
        log.info("redirectUri = {}", redirectUri);
        log.info("resourceUri = {}", resourceUri);

        // HttpHeader와 HttpBody를 하나의 obejct에 담기
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", clientId);
        params.add("redirect_uri", redirectUri);
        params.add("code", code);

        HttpEntity<MultiValueMap<String, String>> accessTokenRequest = new HttpEntity<>(params, headers);

        JsonNode response = restTemplate.exchange(
                resourceUri,
                HttpMethod.POST,
                accessTokenRequest,
                JsonNode.class
        ).getBody();

        return response;
    }

    // 액세스 토큰을 사용하여 로그인한 유저에 대한 정보 요청
    public JsonNode getUserInfoByAccessTokenResponse(JsonNode accessToken) throws JsonProcessingException{

        // json object를 자바에서 처리하기 위한 변환 과정
        ObjectMapper objectMapper = new ObjectMapper();
        KakaoOAuthToken oAuthToken = objectMapper.readValue(accessToken.toString(), KakaoOAuthToken.class);
        log.info("accesstoken = {}", oAuthToken.getAccess_token());

        RestTemplate restTemplate = new RestTemplate();

        // HttpHeader object 생성
        HttpHeaders headers = new HttpHeaders();;
        headers.add("Authorization", "Bearer "+oAuthToken.getAccess_token());
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String, String>> userInfoRequest = new HttpEntity<>(headers);

        String userResourceUri = env.getProperty("oauth.kakao.user-resource-uri");

        return restTemplate.exchange(userResourceUri, HttpMethod.POST, userInfoRequest, JsonNode.class).getBody(); // 유저 정보를 json으로 가져옴.
    }

    // 얻은 유저에 대한 정보에서 필요한 것들만 파싱하여 로그인
    public KakaoLoginDto kakaoLogin(JsonNode userResourceNode) throws JsonProcessingException {
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
                return KakaoLoginDto.builder()
                            .jwtDto(jwtProvider.generateToken(user.getUserId()))
                            .email(user.getEmail())
                            .nickName(user.getNickName())
                            .build();
            } else{
                KakaoLoginDto newKakaoUserDto = KakaoLoginDto.builder()
                        .email(email)
                        .password(bCryptPasswordEncoder.encode(UUID.randomUUID().toString()))
                        .nickName(nickName)
                        .build();
                userRepository.save(new User(newKakaoUserDto));
                return newKakaoUserDto;
            }
        }

        return null;
    }

}
