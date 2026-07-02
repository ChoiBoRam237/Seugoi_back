package com.example.seugoi_back.Study.entity.assignment;

import com.example.seugoi_back.Common.entity.BaseTime;
import com.example.seugoi_back.Study.dto.request.assignment.AsgmtRequestDto;
import com.example.seugoi_back.Study.entity.Study;
import com.example.seugoi_back.User.entity.User;
import com.example.seugoi_back.Util.ListUtil;
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
public class Asgmt extends BaseTime {

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

    public void update(AsgmtRequestDto dto) {
        this.title = dto.getTitle();
        this.content = dto.getContent();
        if (dto.getLinkName() != null) this.linkName = dto.getLinkName();
        else this.linkName = dto.getLinkUrl();
        this.linkUrl = dto.getLinkUrl();
    }
}
