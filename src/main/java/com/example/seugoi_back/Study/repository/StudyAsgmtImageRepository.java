package com.example.seugoi_back.Study.repository;

import com.example.seugoi_back.Study.entity.StudyAsgmtImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudyAsgmtImageRepository extends JpaRepository<StudyAsgmtImage, Long> {
    List<StudyAsgmtImage> findByStudyAsgmt_Code(Long studyAsgmtCode);
    void deleteByStudyAsgmt_Code(Long studyAsgmtCode);
    void deleteByStudy_Code(Long studyCode);
}
