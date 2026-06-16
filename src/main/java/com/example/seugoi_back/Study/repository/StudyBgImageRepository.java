package com.example.seugoi_back.Study.repository;

import com.example.seugoi_back.Study.entity.StudyBgImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudyBgImageRepository extends JpaRepository<StudyBgImage, Long> {
}
