package com.example.seugoi_back.Study.repository.assignment;

import com.example.seugoi_back.Study.entity.assignment.AsgmtImg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AsgmtImgRepository extends JpaRepository<AsgmtImg, Long> {
    List<AsgmtImg> findByAsgmt_Code(Long asgmtCode);
}
