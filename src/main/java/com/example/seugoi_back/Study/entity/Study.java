package com.example.seugoi_back.Study.entity;

import com.example.seugoi_back.Common.entity.BaseTime;
import com.example.seugoi_back.Study.dto.request.StudyRequestDto;
import com.example.seugoi_back.User.entity.User;
import com.example.seugoi_back.Util.ListUtil;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Study extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long code;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_code")
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

    @Builder.Default
    @Column(nullable = false)
    private Long joinCount = 0L; // 가입한 수

    @Builder.Default
    @Column(nullable = false)
    private Long bookmarkCount = 0L; // 북마크 수

    @Builder.Default
    @Column(nullable = false)
    private Long viewCount = 0L; // 조회수

    public void increaseBookmarkCount() {
        this.bookmarkCount++;
    }

    public void decreaseBookmarkCount() {
        this.bookmarkCount--;
    }

    public void increaseJoinCount() {
        this.bookmarkCount++;
    }

    public void decreaseJoinCount() {
        this.bookmarkCount--;
    }
    public void increaseViewCount() {
        this.viewCount++;
    }

    public void update(StudyRequestDto dto) {
        this.studyName = dto.getStudyName();
        this.categories = ListUtil.parseListToString(dto.getCategories());
        this.peopleCount = dto.getPeopleCount();
        this.endPeriod = dto.getEndPeriod();
        this.studyTitle = dto.getStudyTitle();
        this.summary = dto.getSummary();
        this.introduction = ListUtil.parseListToString(dto.getIntroduction());
        this.description = dto.getDescription();
        this.recommend = ListUtil.parseListToString(dto.getRecommend());
    }
}
