package com.example.seugoi_back.Study.repository;

import com.example.seugoi_back.Study.entity.StudyBookmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudyBookmarkRepository extends JpaRepository<StudyBookmark, Long> {
    Optional<StudyBookmark> findByUser_CodeAndStudy_Code(Long userCode, Long studyCode);
    List<StudyBookmark> findByUser_Code(Long userCode);
}
