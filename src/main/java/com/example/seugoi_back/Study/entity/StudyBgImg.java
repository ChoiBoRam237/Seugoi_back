package com.example.seugoi_back.Study.entity;

import com.example.seugoi_back.Common.entity.BaseTime;
import com.example.seugoi_back.User.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudyBgImg extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long code;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_code")
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_code")
    private Study study; // 스터디

    @Column(nullable = false)
    private String imgUrl; // 배경 이미지 url

    public void update(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
