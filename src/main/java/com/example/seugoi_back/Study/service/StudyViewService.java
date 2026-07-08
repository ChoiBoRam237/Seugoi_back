package com.example.seugoi_back.Study.service;

import com.example.seugoi_back.Common.exception.CustomException;
import com.example.seugoi_back.Common.exception.ErrorCode;
import com.example.seugoi_back.Study.dto.response.StudyResponseDto;
import com.example.seugoi_back.Study.entity.Study;
import com.example.seugoi_back.Study.entity.StudyView;
import com.example.seugoi_back.Study.repository.StudyBookmarkRepository;
import com.example.seugoi_back.Study.repository.StudyRepository;
import com.example.seugoi_back.Study.repository.StudyViewRepository;
import com.example.seugoi_back.User.entity.User;
import com.example.seugoi_back.User.service.UserService;
import com.example.seugoi_back.Util.DateUtil;
import com.example.seugoi_back.Util.ListUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class StudyViewService {
    private final StudyRepository studyRepository;
    private final StudyViewRepository studyViewRepository;
    private final StudyBookmarkRepository studyBookmarkRepository;
    private final UserService userService;
    private final StudyBgImgService studyBgImgService;

    @Transactional // 스터디 조회 Service
    public void studyView(Long userCode, Long studyCode) {
        Study study = studyRepository.findById(studyCode)
            .orElseThrow(() -> new CustomException(ErrorCode.STUDY_NOT_FOUND));

        User user = userService.findByUserCode(userCode);

        StudyView studyView = studyViewRepository.findByUserAndStudy(user, study).orElse(null);

        if (studyView == null) {
            StudyView responseDto = StudyView.builder()
                .user(user)
                .study(study)
                .viewedAt(LocalDateTime.now())
                .build();

            studyViewRepository.save(responseDto);

            study.increaseViewCount();
        } else {
            studyView.updateViewedAt();
        }
    }

    @Transactional // 최근에 조회한 스터디 4개 조회 Service
    public List<StudyResponseDto> findStudyLatest(Long userCode) {
        List<StudyView> studyViewList = studyViewRepository.findTop4ByUser_CodeOrderByViewedAtDesc(userCode);

        return studyViewList.stream()
            .map(StudyView::getStudy)
            .map(study -> StudyResponseDto.builder()
                .code(study.getCode())
                .studyName(study.getStudyName())
                .categories(ListUtil.parseStringToList(study.getCategories()))
                .dDay(DateUtil.calculateDDay(study.getEndPeriod()))
                .progress(0)
                .bgImg(studyBgImgService.findByStudyCode(study.getCode()))
                .isAdmin(Objects.equals(userCode, study.getUser().getCode()))
                .isBookmark(studyBookmarkRepository.findByUser_CodeAndStudy_Code(userCode, study.getCode()).isPresent())
                .status(study.getStatus())
                .build())
            .toList();
    }

    @Transactional // 스터디 code에 맞는 조회수 삭제
    public void deleteByStudyCode(Long studyCode) {
        studyViewRepository.deleteByStudy_Code(studyCode);
    }
}
