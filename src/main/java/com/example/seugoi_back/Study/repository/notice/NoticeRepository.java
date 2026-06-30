package com.example.seugoi_back.Study.repository.notice;

import com.example.seugoi_back.Study.entity.notice.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Long> {
    List<Notice> findByStudy_Code(Long studyCode);
    void deleteByStudy_Code(Long studyCode);
}
