package com.example.seugoi_back.Study.repository;

import com.example.seugoi_back.Study.entity.StudyBookmark;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudyBookmarkRepository extends JpaRepository<StudyBookmark, Long> {
    Optional<StudyBookmark> findByUser_CodeAndStudy_Code(Long userCode, Long studyCode);
    List<StudyBookmark> findByUser_Code(Long userCode);
}
