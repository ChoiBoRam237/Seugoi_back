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
public class StudyAsgmt extends BaseTime {

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
    private String title; // 과제 제목

    @Column(length = 200)
    private String content; // 과제 내용

    @Column
    private String linkName; // 링크 이름

    @Column
    private String linkUrl; // 링크 url

    @Builder
    public StudyAsgmt(
        User user,
        Study study,
        String title,
        String content,
        String linkName,
        String linkUrl
    ) {
        this.user = user;
        this.study = study;
        this.title = title;
        this.content = content;
        this.linkName = linkName;
        this.linkUrl = linkUrl;
    }
}
