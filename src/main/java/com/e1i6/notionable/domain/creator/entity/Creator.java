package com.e1i6.notionable.domain.creator.entity;

import com.e1i6.notionable.domain.user.entity.Role;
import com.e1i6.notionable.domain.user.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CREATOR")
public class Creator {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long creator_id;
    private String creatorType; // 입점형태(크리에이터 타입)
    private String bank; // 은행
    private String accountNumber; // 계좌번호
    private String bankPaper; // 통장 사본 링크
    private String identification; // 신분증 사본 링크
    private String status; // accepted, rejected, waited
    private Role role; // ROLE_CREATOR

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    @JsonIgnore
    private User user;
}
