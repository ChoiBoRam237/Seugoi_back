package com.example.seugoi_back.Study.service;

import com.example.seugoi_back.Study.dto.request.CommonStudyRequestDto;
import com.example.seugoi_back.Study.entity.Study;
import com.example.seugoi_back.Study.entity.StudyJoin;
import com.example.seugoi_back.Study.repository.StudyJoinRepository;
import com.example.seugoi_back.Study.repository.StudyRepository;
import com.example.seugoi_back.User.entity.User;
import com.example.seugoi_back.User.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudyJoinService {
    private final UserRepository userRepository;
    private final StudyRepository studyRepository;
    private  final StudyJoinRepository studyJoinRepository;

    @Transactional // 스터디 가입 Service
    public StudyJoin joinStudy(Long userCode, Long studyCode) {
        User user = userRepository.findById(userCode)
            .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));
        Study study = studyRepository.findById(studyCode)
            .orElseThrow(() -> new RuntimeException("스터디를 찾을 수 없습니다."));

        if (studyJoinRepository
            .findByUser_CodeAndStudy_Code(userCode, studyCode)
            .isPresent()
        ) {
            throw new IllegalArgumentException("이미 가입한 사용자입니다.");
        }

        StudyJoin studyJoin = StudyJoin.builder()
            .user(user)
            .study(study)
            .build();

        // 가입한 인원수 증가
        study.increaseJoinCount();

        return studyJoinRepository.save(studyJoin);
    }
}
