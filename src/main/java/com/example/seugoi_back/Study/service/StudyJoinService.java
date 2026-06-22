package com.example.seugoi_back.Study.service;

import com.example.seugoi_back.Study.dto.request.StudyJoinRequestDto;
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

    // 스터디 가입 Service
    @Transactional
    public StudyJoin joinStudy(StudyJoinRequestDto dto) {
        User user = userRepository.findById(dto.getUserCode()).orElseThrow();
        Study study = studyRepository.findById(dto.getStudyCode()).orElseThrow();

        if (studyJoinRepository
                .findByUser_CodeAndStudy_Code(dto.getUserCode(), dto.getStudyCode())
                .isPresent()
        ) {
            throw new IllegalArgumentException("이미 가입한 사용자입니다.");
        }

        StudyJoin studyJoin = StudyJoin.builder()
            .user(user)
            .study(study)
            .build();

        return studyJoinRepository.save(studyJoin);
    }
}
