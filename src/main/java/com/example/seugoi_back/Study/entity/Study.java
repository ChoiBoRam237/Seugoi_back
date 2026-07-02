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
        if(dto.getStudyName() != null) this.studyName = dto.getStudyName();
        if(dto.getPeopleCount() != null) this.peopleCount = dto.getPeopleCount();
        if(dto.getEndPeriod() != null) this.endPeriod = dto.getEndPeriod();
        if(dto.getStudyTitle() != null) this.studyTitle = dto.getStudyTitle();
        if(dto.getSummary() != null) this.summary = dto.getSummary();
        if(dto.getDescription() != null) this.description = dto.getDescription();

        if(dto.getCategories() != null && !dto.getCategories().isEmpty())
            this.categories = ListUtil.parseListToString(dto.getCategories());
        if(dto.getIntroduction() != null && !dto.getIntroduction().isEmpty())
            this.introduction = ListUtil.parseListToString(dto.getIntroduction());
        if(dto.getRecommend() != null && !dto.getRecommend().isEmpty())
            this.recommend = ListUtil.parseListToString(dto.getRecommend());
    }
}
