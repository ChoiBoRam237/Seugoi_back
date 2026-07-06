package com.example.seugoi_back.Study.repository;

import com.example.seugoi_back.Study.entity.Study;
import com.example.seugoi_back.Study.enums.StudyStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface StudyRepository extends JpaRepository<Study, Long> {
    List<Study> findByUser_Code(Long userCode);

    List<Study> findByStudyNameContainingIgnoreCaseOrCategoriesContainingIgnoreCase(String studyName, String categories);
    List<Study> findByEndPeriod(LocalDate endPeriod);
    List<Study> findByEndPeriodBeforeAndStatus(LocalDate endPeriod, StudyStatus status);
}
