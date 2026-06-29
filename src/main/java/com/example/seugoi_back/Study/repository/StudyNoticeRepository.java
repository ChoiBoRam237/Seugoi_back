package com.example.seugoi_back.Study.repository;

import com.example.seugoi_back.Study.entity.StudyNotice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudyNoticeRepository extends JpaRepository<StudyNotice, Long> {
    List<StudyNotice> findByStudy_Code(Long studyCode);
    void deleteByStudy_Code(Long studyCode);
}
