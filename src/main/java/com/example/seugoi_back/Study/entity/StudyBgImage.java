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
public class StudyBgImage extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long code;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_code")
    private Study study; // 스터디

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_code")
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
