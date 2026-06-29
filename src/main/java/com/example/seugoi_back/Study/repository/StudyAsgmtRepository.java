package com.example.seugoi_back.Study.repository;

import com.example.seugoi_back.Study.entity.StudyAsgmt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudyAsgmtRepository extends JpaRepository<StudyAsgmt, Long> {
    List<StudyAsgmt> findAsgmtByStudy_Code(Long studyCode);
    void deleteByStudy_Code(Long studyCode);
}
