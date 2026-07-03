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

import java.util.*;

@Service
@RequiredArgsConstructor
public class StudyBookmarkService {
    private final UserRepository userRepository;
    private final StudyRepository studyRepository;
    private final StudyBookmarkRepository studyBookmarkRepository;
    private final StudyBgImgService studyBgImgService;

    @Transactional // 스터디 북마크 Service
    public Map<String, Object> bookmarkStudy(Long userCode, Long studyCode) {
        User user = userRepository.findById(userCode)
            .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));
        Study study = studyRepository.findById(studyCode)
            .orElseThrow(() -> new RuntimeException("스터디를 찾을 수 없습니다."));

        Optional<StudyBookmark> bookmark =
            studyBookmarkRepository.findByUser_CodeAndStudy_Code(userCode, studyCode);

        // 이미 북마크가 되어 있을 경우 북마크 해제
        if (bookmark.isPresent()) {
            studyBookmarkRepository.delete(bookmark.get());
            // 북마크 수 감소
            study.decreaseBookmarkCount();
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

        // 북마크 수 증가
        study.increaseBookmarkCount();

        return Map.of(
            "code", savedBookmark.getCode(),
            "userCode", savedBookmark.getUser().getCode(),
            "studyCode", savedBookmark.getStudy().getCode(),
            "bookmarked", true
        );
    }

    @Transactional // 내가 북마크한 스터디 조회 Service
    public List<StudyResponseDto> findStudyByBookmark(Long userCode, String sortValue) {
        List<StudyBookmark> studyBookmarkList = studyBookmarkRepository.findByUser_Code(userCode);
        Comparator<StudyBookmark> comparator;

        // 정렬
        switch (sortValue.toUpperCase()) {
            case "NAME":
                comparator = Comparator.comparing(bookmark -> bookmark.getStudy().getStudyName());
                break;

            case "POPULAR":
                comparator = Comparator
                    .comparingLong(
                        (StudyBookmark bookmark) -> bookmark.getStudy().getJoinCount()
                    )
                    .reversed()
                    .thenComparing(
                        Comparator.comparingLong(
                            (StudyBookmark bookmark) -> bookmark.getStudy().getBookmarkCount()
                        ).reversed()
                    )
                    .thenComparing(
                        Comparator.comparingLong(
                            (StudyBookmark bookmark) -> bookmark.getStudy().getViewCount()
                        ).reversed()
                    );
                break;

            case "LATEST":
            default:
                comparator = Comparator.comparing(
                    (StudyBookmark bookmark) -> bookmark.getStudy().getCreatedAt()
                ).reversed();
                break;
        }

        studyBookmarkList = studyBookmarkList.stream()
            .sorted(comparator)
            .toList();

        return studyBookmarkList.stream()
            .map(StudyBookmark::getStudy)
            .map(study -> StudyResponseDto.builder()
                .code(study.getCode())
                .studyName(study.getStudyName())
                .categories(ListUtil.parseStringToList(study.getCategories()))
                .dDay(DateUtil.calculateDDay(study.getEndPeriod()))
                .progress(0)
                .bgImg(studyBgImgService.findByStudyCode(study.getCode()))
                .isAdmin(Objects.equals(userCode, study.getUser().getCode()))
                .isBookmark(studyBookmarkRepository.findByUser_CodeAndStudy_Code(userCode, study.getCode()).isPresent())
                .build())
            .toList();
    }
}
