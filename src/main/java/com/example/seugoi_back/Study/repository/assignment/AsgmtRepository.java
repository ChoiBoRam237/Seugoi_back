package com.example.seugoi_back.Study.repository.assignment;

import com.example.seugoi_back.Study.entity.assignment.Asgmt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AsgmtRepository extends JpaRepository<Asgmt, Long> {
    List<Asgmt> findByStudy_Code(Long studyCode);
    void deleteByStudy_Code(Long studyCode);
}
