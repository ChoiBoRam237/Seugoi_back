package com.example.seugoi_back.Study.repository.assignment;

import com.example.seugoi_back.Study.entity.assignment.AsgmtCmt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AsgmtCmtRepository extends JpaRepository<AsgmtCmt, Long> {
    List<AsgmtCmt> findByUser_CodeAndAsgmt_Code(Long userCode, Long asgmtCode);
    List<AsgmtCmt> findByAsgmt_Code(Long asgmtCode);
    List<AsgmtCmt> findByStudy_Code(Long studyCode);
    boolean existsByAsgmt_CodeAndUser_Code(Long asgmtCode, Long userCode);
    void deleteByStudy_Code(Long studyCode);
    void deleteByAsgmt_Code(Long asgmtCode);
}
