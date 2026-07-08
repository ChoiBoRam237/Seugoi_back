package com.example.seugoi_back.Study.repository;

import com.example.seugoi_back.Study.entity.Study;
import com.example.seugoi_back.Study.entity.StudyView;
import com.example.seugoi_back.User.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudyViewRepository extends JpaRepository<StudyView, Long> {
    Optional<StudyView> findByUserAndStudy(User user, Study study);
    List<StudyView> findTop4ByUser_CodeOrderByViewedAtDesc(Long userCode);
    void deleteByStudy_Code(Long studyCode);
}
