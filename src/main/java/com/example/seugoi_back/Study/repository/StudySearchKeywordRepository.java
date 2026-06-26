package com.example.seugoi_back.Study.repository;

import com.example.seugoi_back.Study.entity.StudySearchKeyword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudySearchKeywordRepository extends JpaRepository<StudySearchKeyword, Long> {
    List<StudySearchKeyword> findByUser_CodeOrderBySearchedAtDesc(Long userCode);
    Optional<StudySearchKeyword> findByUser_CodeAndKeyword(Long userCode, String keyword);
    void deleteByUser_Code(Long userCode);
}
