package com.example.seugoi_back.Study.entity;

import com.example.seugoi_back.Common.entity.BaseTime;
import com.example.seugoi_back.User.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudyNotice extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long code;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_code")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_code")
    private Study study;

    @Column(length = 30)
    private String title; // 공지 제목

    @Column(length = 200)
    private String content; // 공지 내용
}
