package com.example.seugoi_back.Study.service;

import com.example.seugoi_back.Study.dto.request.StudyNoticeRequestDto;
import com.example.seugoi_back.Study.dto.response.StudyListResponseDto;
import com.example.seugoi_back.Study.entity.Study;
import com.example.seugoi_back.Study.entity.StudyNotice;
import com.example.seugoi_back.Study.repository.StudyNoticeRepository;
import com.example.seugoi_back.Study.repository.StudyRepository;
import com.example.seugoi_back.User.entity.User;
import com.example.seugoi_back.User.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class StudyNoticeService {
    private final UserRepository userRepository;
    private final StudyRepository studyRepository;
    private final StudyNoticeRepository studyNoticeRepository;

    @Transactional // 공지 생성 Service
    public StudyNotice generateNotice(Long userCode, Long studyCode, StudyNoticeRequestDto dto) {
        User user = userRepository.findById(userCode).orElseThrow();
        Study study = studyRepository.findById(studyCode).orElseThrow();

        StudyNotice studyNotice = StudyNotice.builder()
            .user(user)
            .study(study)
            .title(dto.getTitle())
            .content(dto.getContent())
            .build();

        return studyNoticeRepository.save(studyNotice);
    }

    @Transactional //  스터디 code에 맞는 모든 공지 조회 Service
    public List<StudyListResponseDto> findNoticeAll(Long userCode, Long studyCode) {
        List<StudyNotice> noticeList = studyNoticeRepository.findByStudy_Code(studyCode);

        List<StudyListResponseDto> responseDto = noticeList.stream()
            .map(item -> StudyListResponseDto.builder()
                .code(item.getCode())
                .title(item.getTitle())
                .content(item.getContent())
                .isAdmin(Objects.equals(item.getUser().getCode(), userCode))
                .createdAt(item.getCreatedAt())
                .build())
            .toList();

        return responseDto;
    }

    @Transactional // 공지 삭제 Service
    public void deleteNotice(Long studyNoticeCode) {
        studyNoticeRepository.deleteById(studyNoticeCode);
    }

    @Transactional // 스터디 code에 맞는 공지 삭제
    public void deleteNoticeByStudyCode(Long studyCode) {
        studyNoticeRepository.deleteByStudy_Code(studyCode);
    }
}
