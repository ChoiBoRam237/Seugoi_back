package com.example.seugoi_back.User.entity;

import com.example.seugoi_back.Common.entity.BaseTime;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Getter
@NoArgsConstructor
public class User extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long code;

    @Column(nullable = false)
    private String kakaoId; // 카카오 아이디

    @Column(nullable = false)
    private String email; // 이메일

    @Column
    private String nickname; // 사용자 이름

    @Column
    private String profileImageUrl; // 프로필 이미지 url

    @Builder
    public User(
            String kakaoId,
            String email,
            String nickname,
            String profileImageUrl
    ) {
        this.kakaoId = kakaoId;
        this.email = email;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
    }
}
