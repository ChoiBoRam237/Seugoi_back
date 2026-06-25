package com.example.seugoi_back.Jwt.repository;

import com.example.seugoi_back.Jwt.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
}
