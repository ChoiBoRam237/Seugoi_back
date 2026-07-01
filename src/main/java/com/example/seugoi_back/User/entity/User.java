package com.example.seugoi_back.User.entity;

import com.example.seugoi_back.Common.entity.BaseTime;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long code;

    @Column(nullable = false)
    private String kakaoId; // 카카오 아이디

    @Column(nullable = false)
    private String email; // 이메일

    @Column
    private String name; // 사용자 이름

    @Column
    private String profileImgUrl; // 프로필 이미지 url
}
