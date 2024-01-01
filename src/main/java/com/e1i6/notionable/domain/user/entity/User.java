package com.e1i6.notionable.domain.user.entity;

import javax.persistence.*;

import com.e1i6.notionable.domain.review.entity.Review;
import com.e1i6.notionable.domain.template.entity.Template;
import com.e1i6.notionable.domain.user.data.dto.request.SocialLoginReqDto;
import com.e1i6.notionable.global.common.entity.BaseTimeEntity;
import com.sun.istack.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long userId;
	@NotNull
	@Column(unique = true)
	private String email;
	private String password;
	private Integer userType;
	@Enumerated(EnumType.STRING)
	private Role role;
	private String nickName;
	private String profile;
	private String phoneNumber;

	@OneToMany(mappedBy = "user", cascade = {CascadeType.REMOVE, CascadeType.REFRESH}, orphanRemoval = true)
	private List<Template> templateList = new ArrayList<>();

	@OneToMany(mappedBy = "user", cascade = {CascadeType.REMOVE, CascadeType.REFRESH}, orphanRemoval = true)
	private List<Review> reviewList = new ArrayList<>();

	public User(SocialLoginReqDto socialLoginReqDto) {
		this.email = socialLoginReqDto.getEmail();
		this.password = socialLoginReqDto.getPassword();
		this.userType = socialLoginReqDto.getUserType();
		this.nickName = socialLoginReqDto.getNickName();
		this.profile = socialLoginReqDto.getProfile();
		this.phoneNumber = socialLoginReqDto.getPhoneNumber();
		//role
	}
}
