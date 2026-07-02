package com.example.seugoi_back.Study.service.notice;

import com.example.seugoi_back.Study.dto.request.notice.NoticeRequestDto;
import com.example.seugoi_back.Study.dto.response.CommonStudyResponseDto;
import com.example.seugoi_back.Study.dto.response.StudyBoardResponseDto;
import com.example.seugoi_back.Study.entity.Study;
import com.example.seugoi_back.Study.entity.notice.Notice;
import com.example.seugoi_back.Study.repository.notice.NoticeRepository;
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
public class NoticeService {
    private final UserRepository userRepository;
    private final StudyRepository studyRepository;
    private final NoticeRepository noticeRepository;

    @Transactional // 공지 생성 Service
    public Notice generateNotice(Long userCode, Long studyCode, NoticeRequestDto dto) {
        User user = userRepository.findById(userCode).orElseThrow();
        Study study = studyRepository.findById(studyCode).orElseThrow();

        Notice notice = Notice.builder()
            .user(user)
            .study(study)
            .title(dto.getTitle())
            .content(dto.getContent())
            .build();

        return noticeRepository.save(notice);
    }

    @Transactional //  스터디 code에 맞는 모든 공지 조회 Service
    public List<StudyBoardResponseDto> findByStudyCode(Long userCode, Long studyCode) {
        List<Notice> noticeList = noticeRepository.findByStudy_Code(studyCode);

        List<StudyBoardResponseDto> responseDto = noticeList.stream()
            .map(item -> StudyBoardResponseDto.builder()
                .code(item.getCode())
                .target("notice")
                .title(item.getTitle())
                .content(item.getContent())
                .isAdmin(Objects.equals(item.getUser().getCode(), userCode))
                .createdAt(item.getCreatedAt())
                .build())
            .toList();

        return responseDto;
    }

    @Transactional // 특정 공지 조회 Service
    public StudyBoardResponseDto findByNoticeCode(Long userCode, Long noticeCode) {
        Notice notice = noticeRepository.findByUser_CodeAndCode(userCode, noticeCode)
            .orElseThrow(() -> new RuntimeException("공지를 찾을 수 없습니다"));

        return StudyBoardResponseDto.builder()
            .code(notice.getCode())
            .studyCode(notice.getStudy().getCode())
            .title(notice.getTitle())
            .content(notice.getContent())
            .build();
    }

    @Transactional // 공지 수정 Service
    public CommonStudyResponseDto updateNotice(Long noticeCode, NoticeRequestDto dto) {
        Notice notice = noticeRepository.findById(noticeCode)
            .orElseThrow(() -> new RuntimeException("공지를 찾을 수 없습니다"));

        notice.update(dto);

        return CommonStudyResponseDto.builder()
                .code(notice.getCode())
                .userCode(notice.getUser().getCode())
                .studyCode(notice.getStudy().getCode())
                .build();
    }

    @Transactional // 공지 삭제 Service
    public void deleteByNoticeCode(Long noticeCode) {
        noticeRepository.deleteById(noticeCode);
    }

    @Transactional // 스터디 code에 맞는 공지 삭제
    public void deleteByStudyCode(Long studyCode) {
        noticeRepository.deleteByStudy_Code(studyCode);
    }
}
