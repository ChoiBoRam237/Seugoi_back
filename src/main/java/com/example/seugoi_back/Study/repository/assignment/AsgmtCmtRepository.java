package com.example.seugoi_back.Study.repository.assignment;

import com.example.seugoi_back.Study.entity.assignment.AsgmtCmt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AsgmtCmtRepository extends JpaRepository<AsgmtCmt, Long> {
    List<AsgmtCmt> findByUser_CodeAndAsgmt_Code(Long userCode, Long asgmtCode);
    List<AsgmtCmt> findByAsgmt_Code(Long asgmtCode);
    List<AsgmtCmt> findByStudy_Code(Long studyCode);
    List<AsgmtCmt> findByUser_CodeAndStudy_Code(Long userCode, Long studyCode);
    boolean existsByAsgmt_CodeAndUser_Code(Long asgmtCode, Long userCode);
    void deleteByStudy_Code(Long studyCode);
    void deleteByAsgmt_Code(Long asgmtCode);
    void deleteByUser_CodeAndStudy_Code(Long userCode, Long studyCode);
    @Query("""
        SELECT c
        FROM AsgmtCmt c
        JOIN FETCH c.asgmt a
        JOIN FETCH a.study s
        WHERE c.user.code = :userCode
          AND s.user.code <> :userCode
        ORDER BY c.createdAt DESC
    """)
    List<AsgmtCmt> findStudying(Long userCode);
}
