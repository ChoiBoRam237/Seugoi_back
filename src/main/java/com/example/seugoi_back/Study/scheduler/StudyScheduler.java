package com.example.seugoi_back.Study.scheduler;

import com.example.seugoi_back.Study.entity.Study;
import com.example.seugoi_back.Study.enums.StudyStatus;
import com.example.seugoi_back.Study.repository.StudyRepository;
import com.example.seugoi_back.Study.service.StudyService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class StudyScheduler {
    private final StudyRepository studyRepository;
    private final StudyService studyService;

    /**
     * 매일 0시에 실행
     * 종료기간 이후 10일이 지나면 자동으로 삭제
     */
    @Scheduled(cron = "0 0 0 * * *")
    public void deleteExpiredStudy() {
        LocalDate targetDate = LocalDate.now().minusDays(10);
        List<Study> studyList = studyRepository.findByEndPeriod(targetDate);
        studyList.forEach(study ->
            studyService.deleteByStudyCode(study.getCode())
        );
    }

    /**
     * 매일 0시에 실행
     * 오늘이 종료기간인 스터디 Status FINISHED 로 변경
     */
    @Transactional
    @Scheduled(cron = "0 0 0 * * *")
    public void updateFinishedStudy() {
        LocalDate today = LocalDate.now();
        List<Study> studyList = studyRepository.findByEndPeriodBeforeAndStatus(today, StudyStatus.STUDYING);
        studyList.forEach(study ->
            study.updateStatus(StudyStatus.FINISHED)
        );
    }
}
