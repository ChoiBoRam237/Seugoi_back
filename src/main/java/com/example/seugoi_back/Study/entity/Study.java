package com.example.seugoi_back.Study.entity;

import com.example.seugoi_back.User.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Getter
@NoArgsConstructor
public class Study {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String studyName; // 스터디 이름

    @Column(length = 100)
    private String categories; // 카테고리 (배열)

    @Column(nullable = false)
    private String peopleCount; // 인원수 (제한 없음: -)

    @Column
    private Date endPeriod; // 스터디 종료 기간

    @Column(columnDefinition = "TEXT")
    private String summary; // 스터디 간단 요약

    @Column(length = 255)
    private String introduction; // 스터디 소개글 (배열)

    @Column(columnDefinition = "TEXT")
    private String description; // 스터디 설명

    @Column(length = 255)
    private String recommend; // 추천할 유형 (배열)

    @Builder
    public Study(
            User user,
            String studyName,
            String categories,
            String peopleCount,
            Date endPeriod,
            String summary,
            String introduction,
            String description,
            String recommend
    ) {
        this.user = user;
        this.studyName = studyName;
        this.categories = categories;
        this.peopleCount = peopleCount;
        this.endPeriod = endPeriod;
        this.summary = summary;
        this.introduction = introduction;
        this.description = description;
        this.recommend = recommend;
    }
}
