package com.example.seugoi_back.Study.repository;

import com.example.seugoi_back.Study.entity.StudyAsgmtImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudyAsgmtImageRepository extends JpaRepository<StudyAsgmtImage, Long> {
    Optional<StudyAsgmtImage> findByStudyAsgmt_Code(Long studyAsgmtCode);
    void deleteAllByStudyAsgmt_Code(Long studyAsgmtCode);
}
