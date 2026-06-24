package com.example.seugoi_back.Study.repository;

import com.example.seugoi_back.Study.entity.StudyAsgmtImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudyAsgmtImageRepository extends JpaRepository<StudyAsgmtImage, Long> {
    Optional<StudyAsgmtImage> findByStudyAsgmt_Code(Long studyAsgmtCode);
}
