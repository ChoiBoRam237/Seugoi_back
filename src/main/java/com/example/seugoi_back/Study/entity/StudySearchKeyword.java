package com.example.seugoi_back.Study.entity;

import com.example.seugoi_back.User.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudySearchKeyword {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long code;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_code")
    private User user;

    @Column(length = 255, nullable = false)
    private String keyword; // 검색어

    @Column
    private LocalDateTime searchedAt;

    public void updateSearchedAt() {
        this.searchedAt = LocalDateTime.now();
    }
}
