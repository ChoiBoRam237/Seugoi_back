package com.example.seugoi_back.Study.repository;

import com.example.seugoi_back.Study.entity.Study;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudyRepository extends JpaRepository<Study, Long> {
    List<Study> findByUser_Code(Long userCode);

    List<Study> findByStudyNameContainingIgnoreCaseOrCategoriesContainingIgnoreCase(String studyName, String categories);
    void deleteByUser_CodeAndCode(Long userCode, Long studyCode);
}
