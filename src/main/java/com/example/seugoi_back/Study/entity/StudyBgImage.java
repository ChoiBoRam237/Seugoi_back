package com.example.seugoi_back.Study.entity;

import com.example.seugoi_back.User.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class StudyBgImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_id")
    private Study study; // 스터디

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String studyBgImgUrl; // 스터디 배경 이미지 url

    @Builder
    public StudyBgImage(
            Study study,
            User user,
            String studyBgImgUrl
    ) {
        this.study = study;
        this.user = user;
        this.studyBgImgUrl = studyBgImgUrl;
    }
}
