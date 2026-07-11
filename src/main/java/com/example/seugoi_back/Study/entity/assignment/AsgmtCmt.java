package com.example.seugoi_back.Study.entity.assignment;

import com.example.seugoi_back.Common.entity.BaseTime;
import com.example.seugoi_back.Study.dto.request.assignment.AsgmtCmtRequestDto;
import com.example.seugoi_back.Study.entity.Study;
import com.example.seugoi_back.User.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AsgmtCmt extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long code;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_code")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_code")
    private Study study;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "asgmt_code")
    private Asgmt asgmt;

    @Column(length = 255)
    private String comment; // 댓글 내용

    @Column(nullable = false)
    @Builder.Default
    private boolean ownerCheck = false; // 관리자 체크 여부

    public void update(AsgmtCmtRequestDto dto) {
        this.comment = dto.getComment();
    }
}
