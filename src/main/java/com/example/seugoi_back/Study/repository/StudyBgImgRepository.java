package com.example.seugoi_back.Study.repository;

import com.example.seugoi_back.Study.entity.StudyBgImg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudyBgImgRepository extends JpaRepository<StudyBgImg, Long> {
    Optional<StudyBgImg> findByStudy_Code(Long studyCode);
    void deleteByStudy_Code(Long studyCode);
}
