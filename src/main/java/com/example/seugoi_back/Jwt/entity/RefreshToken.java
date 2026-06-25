package com.example.seugoi_back.Jwt.entity;

import com.example.seugoi_back.Common.entity.BaseTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
public class RefreshToken extends BaseTime {

    @Id
    private Long userCode;

    @Column(nullable = false)
    private String refreshToken; // 리프레시 토큰

    // 만료 시간
    @Column
    private LocalDateTime expiredAt;
}
