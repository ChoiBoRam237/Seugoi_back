package com.example.seugoi_back.Study.entity;

import com.example.seugoi_back.User.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

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
    private String peopleCount; // 모집 인원 (제한 없음: -)

    @Column(length = 10)
    private String endPeriod; // 스터디 종료 기간

    @Column(length = 100)
    private String studyTitle; // 스터디 제목

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
            String endPeriod,
            String studyTitle,
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
        this.studyTitle = studyTitle;
        this.summary = summary;
        this.introduction = introduction;
        this.description = description;
        this.recommend = recommend;
    }
}
