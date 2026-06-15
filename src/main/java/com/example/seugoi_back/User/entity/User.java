package com.example.seugoi_back.User.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String kakaoId;

    @NotNull
    private String email;

    private String nickname;

    private String profileImageUrl;

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
