package com.example.seugoi_back.Study.service;

import com.example.seugoi_back.Study.dto.request.StudyRequestDto;
import com.example.seugoi_back.Study.dto.response.StudyDetailResponseDto;
import com.example.seugoi_back.Study.dto.response.StudyResponseDto;
import com.example.seugoi_back.Study.entity.Study;
import com.example.seugoi_back.Study.entity.StudyBgImg;
import com.example.seugoi_back.Study.entity.StudyJoin;
import com.example.seugoi_back.Study.repository.*;
import com.example.seugoi_back.Study.service.assignment.AsgmtService;
import com.example.seugoi_back.Study.service.notice.NoticeService;
import com.example.seugoi_back.User.entity.User;
import com.example.seugoi_back.User.repository.UserRepository;
import com.example.seugoi_back.Util.DateUtil;
import com.example.seugoi_back.Util.ListUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.util.*;

@Service
@RequiredArgsConstructor
public class StudyService {
    private final UserRepository userRepository;
    private final StudyRepository studyRepository;
    private final StudyBgImgRepository studyBgImageRepository;
    private final StudyJoinRepository studyJoinRepository;
    private final StudyBookmarkRepository studyBookmarkRepository;
    private final StudyBgImgService studyBgImgService;
    private final AsgmtService asgmtService;
    private final NoticeService noticeService;
    private final StudyViewService studyViewService;
    private final StudySearchKeywordService studySearchKeywordService;

    ObjectMapper mapper = new ObjectMapper();

    @Transactional // 스터디 생성 Service
    public Study generateStudy(Long userCode, StudyRequestDto dto) {
        User user = userRepository.findById(userCode).orElseThrow();
        // 배열 -> String
        String categoriesJson = mapper.writeValueAsString(dto.getCategories());
        String introductionJson = mapper.writeValueAsString(dto.getIntroduction());
        String recommendJson = mapper.writeValueAsString(dto.getRecommend());

        // 스터디 정보 저장
        Study study = Study.builder()
            .user(user)
            .studyName(dto.getStudyName())
            .categories(categoriesJson)
            .peopleCount(dto.getPeopleCount())
            .endPeriod(dto.getEndPeriod())
            .studyTitle(dto.getStudyTitle())
            .summary(dto.getSummary())
            .introduction(introductionJson)
            .description(dto.getDescription())
            .recommend(recommendJson)
            .build();
        Study savedStudy = studyRepository.save(study);

        // 스터디 배경 이미지 저장
        String studyBgImageUrl = studyBgImgService.saveBgImage(dto.getBgImageUrl());
        StudyBgImg studyBgImage = StudyBgImg.builder()
            .study(savedStudy)
            .user(user)
            .imgUrl(studyBgImageUrl)
            .build();
        studyBgImageRepository.save(studyBgImage);

        return savedStudy;
    }

    @Transactional // 모든 스터디 조회 Service
    public List<StudyResponseDto> findStudyAll(Long userCode, String filterValue, String sortValue) {
        List<Study> studyList;

        // 필터
        switch (filterValue.toUpperCase()) {
            case "MY_STUDY":
                studyList = studyRepository.findByUser_Code(userCode);
            break;

            case "JOINED":
                studyList = studyJoinRepository.findByUser_Code(userCode)
                    .stream()
                    .map(StudyJoin::getStudy)
                    .toList();
            break;

            case "ALL":
            default:
                studyList = studyRepository.findAll();
            break;
        }

        // 정렬
        switch (sortValue.toUpperCase()) {
            case "NAME":
                studyList.stream()
                    .sorted(Comparator.comparing(Study::getStudyName)).toList();
            break;

            case "POPULAR":
                studyList = studyList.stream()
                    .sorted(
                        Comparator.comparingLong(Study::getJoinCount)
                            .reversed()
                            .thenComparing(
                                Comparator.comparingLong(Study::getBookmarkCount).reversed()
                            )
                            .thenComparing(
                                Comparator.comparingLong(Study::getViewCount).reversed()
                            )
                            .thenComparing(Study::getStudyName)
                    )
                    .toList();
                break;

            case "LATEST":
            default:
                studyList.stream()
                    .sorted(Comparator.comparing(Study::getCreatedAt)).toList();
            break;
        }

        List<StudyResponseDto> responseDto = studyList.stream()
            .map(item -> StudyResponseDto.builder()
                .code(item.getCode())
                .studyName(item.getStudyName())
                .categories(ListUtil.parseStringList(item.getCategories()))
                .dDay(DateUtil.calculateDDay(item.getEndPeriod()))
                .progress(0)
                .bgImageUrl(studyBgImgService.findByStudyCode(item.getCode()).getImgUrl())
                .isAdmin(Objects.equals(userCode, item.getUser().getCode()))
                .isBookmark(studyBookmarkRepository.findByUser_CodeAndStudy_Code(userCode, item.getCode()).isPresent())
                .build())
            .toList();

        return responseDto;
    }

    @Transactional // 특정 스터디 조회 Service
    public Map<String, Object> findByStudyCode(Long userCode, Long studyCode) {
        Study study = studyRepository.findById(studyCode)
            .orElseThrow(() -> new RuntimeException("스터디를 찾을 수 없습니다."));

        // 배경 이미지
        StudyBgImg bgImage = studyBgImgService.findByStudyCode(studyCode);

        // 내가 이 스터디에 가입했는지 안했는지
        boolean isJoined = studyJoinRepository
            .findByUser_CodeAndStudy_Code(userCode, studyCode)
            .isPresent();

        // 내가 이 스터디 북마크했는지 안했는지
        boolean isBookmark = studyBookmarkRepository
            .findByUser_CodeAndStudy_Code(userCode, studyCode)
            .isPresent();

        // 관리자 정보
        User adminResponseDto = User.builder()
            .name(study.getUser().getName())
            .profileImgUrl(study.getUser().getProfileImgUrl())
            .build();

        StudyDetailResponseDto studyResponseDto =
            StudyDetailResponseDto.builder()
                .code(study.getCode())
                .studyName(study.getStudyName())
                .categories(ListUtil.parseStringList(study.getCategories()))
                .peopleCount(study.getPeopleCount())
                .joinCount(study.getJoinCount())
                .dDay(DateUtil.calculateDDay(study.getEndPeriod()))
                .studyTitle(study.getStudyTitle())
                .summary(study.getSummary())
                .introduction(ListUtil.parseStringList(study.getIntroduction()))
                .description(study.getDescription())
                .recommend(ListUtil.parseStringList(study.getRecommend()))
                .bgImageUrl(bgImage.getImgUrl())
                .isJoined(isJoined)
                .isBookmark(isBookmark)
                .build();

        // 조회수 증가
        studyViewService.studyView(userCode, studyCode);

        return Map.of(
            "admin", adminResponseDto,
            "study", studyResponseDto,
            "isAdmin", Objects.equals(userCode, study.getUser().getCode())
        );
    }

    @Transactional // 스터디 검색 Service
    public List<StudyResponseDto> findByKeyword(Long userCode, String keyword) {
        List<Study> studyList = studyRepository.findByStudyNameContainingIgnoreCaseOrCategoriesContainingIgnoreCase(keyword, keyword);

        List<StudyResponseDto> responseDto = studyList.stream()
            .map(item -> StudyResponseDto.builder()
                .code(item.getCode())
                .studyName(item.getStudyName())
                .categories(ListUtil.parseStringList(item.getCategories()))
                .dDay(DateUtil.calculateDDay(item.getEndPeriod()))
                .progress(0)
                .bgImageUrl(studyBgImgService.findByStudyCode(item.getCode()).getImgUrl())
                .isAdmin(Objects.equals(userCode, item.getUser().getCode()))
                .isBookmark(studyBookmarkRepository.findByUser_CodeAndStudy_Code(userCode, item.getCode()).isPresent())
                .build())
            .toList();

        // 검색어 저장
        studySearchKeywordService.saveSearchKeyword(userCode, keyword);

        return responseDto;
    }

    @Transactional // 요즘 뜨고있는 스터디 조회 Service
    public List<StudyResponseDto> findStudyTrend(Long userCode) {
        List<Study> studyList = studyRepository.findAll();

        // 모든 스터디의 가입자, 북마크, 조회수가 0이면 빈 배열 반환
        boolean hasTrendStudy = studyList.stream()
            .anyMatch(study ->
                study.getJoinCount() > 0 ||
                study.getBookmarkCount() > 0 ||
                study.getViewCount() > 0
            );

        if (!hasTrendStudy) {
            return Collections.emptyList();
        }

        List<StudyResponseDto> responseDto = studyList.stream()
            .sorted(
                Comparator.comparingLong(Study::getJoinCount)
                .reversed()
                .thenComparing(
                    Comparator.comparingLong(Study::getBookmarkCount)
                        .reversed()
                )
                .thenComparing(
                    Comparator.comparingLong(Study::getViewCount)
                        .reversed()
                )
                .thenComparing(Study::getStudyName)
            )
            .limit(8)
            .map(study -> StudyResponseDto.builder()
                .code(study.getCode())
                .studyName(study.getStudyName())
                .categories(ListUtil.parseStringList(study.getCategories()))
                .dDay(DateUtil.calculateDDay(study.getEndPeriod()))
                .progress(0)
                .bgImageUrl(studyBgImgService.findByStudyCode(study.getCode()).getImgUrl())
                .isAdmin(Objects.equals(userCode, study.getUser().getCode()))
                .isBookmark(studyBookmarkRepository.findByUser_CodeAndStudy_Code(userCode, study.getCode()).isPresent())
                .build())
            .toList();

        return responseDto;
    }

    @Transactional // 스터디 삭제 Service
    public void deleteByStudyCode(Long studyCode) {
        asgmtService.deleteByStudyCode(studyCode); // 과제 삭제
        noticeService.deleteByStudyCode(studyCode); // 공지 삭제
        studyRepository.deleteById(studyCode); // 스터디 삭제
    }
}
