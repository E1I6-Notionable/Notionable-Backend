package com.e1i6.notionable.domain.user.entity;

import javax.persistence.*;

import com.e1i6.notionable.domain.cart.entity.Cart;
import com.e1i6.notionable.domain.creator.entity.Creator;
import com.e1i6.notionable.domain.inquiry.entity.Inquiry;
import com.e1i6.notionable.domain.review.entity.Review;
import com.e1i6.notionable.domain.template.entity.Template;
import com.e1i6.notionable.domain.user.data.dto.UserDto;
import com.e1i6.notionable.domain.user.data.dto.request.AddSocialLoginUserReqDto;
import com.e1i6.notionable.global.common.entity.BaseTimeEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
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

	@JsonIgnore
	@OneToMany(mappedBy = "user", cascade = {CascadeType.REMOVE, CascadeType.REFRESH}, orphanRemoval = true)
	private List<Template> templateList = new ArrayList<>();

	@JsonIgnore
	@OneToMany(mappedBy = "user", cascade = {CascadeType.REMOVE, CascadeType.REFRESH}, orphanRemoval = true)
	private List<Review> reviewList = new ArrayList<>();

	@JsonIgnore
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<Cart> cartItems;

	@JsonIgnore
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Inquiry> inquiries = new ArrayList<>();

	public void changePassword(String newPassword) {
		this.password = newPassword;
	}

	public User(AddSocialLoginUserReqDto addSocialLoginUserReqDto) {
		this.email = addSocialLoginUserReqDto.getEmail();
		this.password = addSocialLoginUserReqDto.getPassword();
		this.userType = addSocialLoginUserReqDto.getUserType();
		this.nickName = addSocialLoginUserReqDto.getNickName();
		this.profile = addSocialLoginUserReqDto.getProfile();
		this.phoneNumber = addSocialLoginUserReqDto.getPhoneNumber();
		this.creator = new Creator();
		//role
	}

	public void modifyUserProfile(UserDto userDto) {
		this.nickName = userDto.getNickName();
		this.profile = userDto.getProfile();
	}

	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
	private Creator creator;

	public void setCreator(Creator creator) {
		this.creator = creator;
	}
}
