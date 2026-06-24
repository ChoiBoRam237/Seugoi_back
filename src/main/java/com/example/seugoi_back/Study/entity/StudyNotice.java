package com.example.seugoi_back.Study.entity;

import com.example.seugoi_back.Common.entity.BaseTime;
import com.example.seugoi_back.User.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
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

    @Builder
    public StudyNotice(
        User user,
        Study study,
        String title,
        String content
    ) {
        this.user = user;
        this.study = study;
        this.title = title;
        this.content = content;
    }
}
