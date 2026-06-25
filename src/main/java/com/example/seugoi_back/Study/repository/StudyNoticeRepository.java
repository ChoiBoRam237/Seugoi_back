package com.example.seugoi_back.Study.repository;

import com.example.seugoi_back.Study.entity.StudyNotice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudyNoticeRepository extends JpaRepository<StudyNotice, Long> {
}
