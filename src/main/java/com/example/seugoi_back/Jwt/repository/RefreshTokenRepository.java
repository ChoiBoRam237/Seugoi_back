package com.example.seugoi_back.Jwt.repository;

import com.example.seugoi_back.Jwt.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
}
