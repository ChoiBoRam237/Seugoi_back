package com.example.seugoi_back.Study.repository.assignment;

import com.example.seugoi_back.Study.entity.assignment.AsgmtCmtImg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AsgmtCmtImgRepository extends JpaRepository<AsgmtCmtImg, Long> {
    List<AsgmtCmtImg> findByAsgmtCmt_Code(Long asgmtCmtCode);
    void deleteByAsgmtCmt_Code(Long asgmtCmtCode);
}
