package com.example.seugoi_back.Study.service;

import com.example.seugoi_back.Study.dto.response.StudyResponseDto;
import com.example.seugoi_back.Study.entity.Study;
import com.example.seugoi_back.Study.entity.StudyBookmark;
import com.example.seugoi_back.Study.repository.StudyBookmarkRepository;
import com.example.seugoi_back.Study.repository.StudyRepository;
import com.example.seugoi_back.User.entity.User;
import com.example.seugoi_back.User.repository.UserRepository;
import com.example.seugoi_back.Util.DateUtil;
import com.example.seugoi_back.Util.ListUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudyBookmarkService {
    private final UserRepository userRepository;
    private final StudyRepository studyRepository;
    private final StudyBookmarkRepository studyBookmarkRepository;
    private final StudyBgImageService studyBgImageService;

    @Transactional // 스터디 북마크 Service
    public Map<String, Object> bookmarkStudy(Long userCode, Long studyCode) {
        User user = userRepository.findById(userCode).orElseThrow();
        Study study = studyRepository.findById(studyCode).orElseThrow();

        Optional<StudyBookmark> bookmark =
            studyBookmarkRepository.findByUser_CodeAndStudy_Code(userCode, studyCode);

        // 이미 북마크가 되어 있을 경우 북마크 해제
        if (bookmark.isPresent()) {
            studyBookmarkRepository.delete(bookmark.get());
            return Map.of(
                "userCode", user.getCode(),
                "studyCode", study.getCode(),
                "bookmarked", false
            );
        }

        StudyBookmark studyBookmark = StudyBookmark.builder()
            .user(user)
            .study(study)
            .build();

        StudyBookmark savedBookmark = studyBookmarkRepository.save(studyBookmark);

        return Map.of(
            "code", savedBookmark.getCode(),
            "userCode", savedBookmark.getUser().getCode(),
            "studyCode", savedBookmark.getStudy().getCode(),
            "bookmarked", true
        );
    }

    @Transactional // 내가 북마크한 스터디 조회 Service
    public List<StudyResponseDto> findStudyByBookmark(Long userCode) {
        List<StudyBookmark> studyBookmarkList = studyBookmarkRepository.findByUser_Code(userCode);

        return studyBookmarkList.stream()
                .map(StudyBookmark::getStudy)
                .map(study -> StudyResponseDto.builder()
                    .code(study.getCode())
                    .studyName(study.getStudyName())
                    .categories(ListUtil.parseStringList(study.getCategories()))
                    .dDay(DateUtil.calculateDDay(study.getEndPeriod()))
                    .progress(0)
                    .bgImageUrl(studyBgImageService.findBgImageByCode(study.getCode()).getStudyBgImgUrl())
                    .isAdmin(Objects.equals(userCode, study.getUser().getCode()))
                    .isBookmark(studyBookmarkRepository.findByUser_CodeAndStudy_Code(userCode, study.getCode()).isPresent())
                    .build())
                .toList();
    }
}
