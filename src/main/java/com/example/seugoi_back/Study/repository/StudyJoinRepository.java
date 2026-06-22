package com.example.seugoi_back.Study.repository;

import com.example.seugoi_back.Study.entity.StudyJoin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudyJoinRepository extends JpaRepository<StudyJoin, Long> {
    Optional<StudyJoin> findByUser_Code(Long userCode);
    List<StudyJoin> findByStudy_Code(Long studyCode);

    Optional<StudyJoin> findByUser_CodeAndStudy_Code(Long userCode, Long studyCode);
}
