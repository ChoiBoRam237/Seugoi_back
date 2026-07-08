package com.example.seugoi_back.Study.service;

import com.example.seugoi_back.Chat.service.ChatRoomService;
import com.example.seugoi_back.Common.exception.CustomException;
import com.example.seugoi_back.Common.exception.ErrorCode;
import com.example.seugoi_back.Common.response.CommonImgResponseDto;
import com.example.seugoi_back.Study.dto.request.StudyRequestDto;
import com.example.seugoi_back.Study.dto.response.CommonStudyResponseDto;
import com.example.seugoi_back.Study.dto.response.StudyDetailResponseDto;
import com.example.seugoi_back.Study.dto.response.StudyResponseDto;
import com.example.seugoi_back.Study.entity.Study;
import com.example.seugoi_back.Study.entity.StudyBgImg;
import com.example.seugoi_back.Study.entity.StudyJoin;
import com.example.seugoi_back.Study.entity.assignment.AsgmtCmt;
import com.example.seugoi_back.Study.repository.*;
import com.example.seugoi_back.Study.repository.assignment.AsgmtCmtRepository;
import com.example.seugoi_back.Study.service.assignment.AsgmtCmtService;
import com.example.seugoi_back.Study.service.assignment.AsgmtService;
import com.example.seugoi_back.Study.service.notice.NoticeService;
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
public class StudyService {
    private final UserRepository userRepository;
    private final StudyRepository studyRepository;
    private final StudyBgImgRepository studyBgImageRepository;
    private final StudyJoinRepository studyJoinRepository;
    private final StudyBookmarkRepository studyBookmarkRepository;
    private final AsgmtCmtRepository asgmtCmtRepository;
    private final StudyBgImgService studyBgImgService;
    private final AsgmtService asgmtService;
    private final AsgmtCmtService asgmtCmtService;
    private final NoticeService noticeService;
    private final StudyViewService studyViewService;
    private final StudyJoinService studyJoinService;
    private final StudyBookmarkService studyBookmarkService;
    private final StudySearchKeywordService studySearchKeywordService;
    private final ChatRoomService chatRoomService;

    @Transactional // 스터디 생성 Service
    public Study generateStudy(Long userCode, StudyRequestDto dto) {
        User user = userRepository.findById(userCode)
            .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        // 배열 -> String
        String categoriesJson = ListUtil.parseListToString(dto.getCategories());
        String introductionJson = ListUtil.parseListToString(dto.getIntroduction());
        String recommendJson = ListUtil.parseListToString(dto.getRecommend());

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
        String studyBgImageUrl = studyBgImgService.saveBgImage(dto.getImgUrl());
        StudyBgImg studyBgImage = StudyBgImg.builder()
            .study(savedStudy)
            .user(user)
            .folderName("/uploads/study/")
            .imgUrl(studyBgImageUrl)
            .build();
        studyBgImageRepository.save(studyBgImage);

        // 채팅방 자동 생성
        chatRoomService.generateChatRoom(userCode, savedStudy.getCode(), savedStudy.getStudyName());

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
                .categories(ListUtil.parseStringToList(item.getCategories()))
                .dDay(DateUtil.calculateDDay(item.getEndPeriod()))
                .progress(0)
                .bgImg(studyBgImgService.findByStudyCode(item.getCode()))
                .isAdmin(Objects.equals(userCode, item.getUser().getCode()))
                .isBookmark(studyBookmarkRepository.findByUser_CodeAndStudy_Code(userCode, item.getCode()).isPresent())
                .status(item.getStatus())
                .build())
            .toList();

        return responseDto;
    }

    @Transactional // 특정 스터디 조회 Service
    public Map<String, Object> findByStudyCode(Long userCode, Long studyCode) {
        Study study = studyRepository.findById(studyCode)
            .orElseThrow(() -> new CustomException(ErrorCode.STUDY_NOT_FOUND));

        // 배경 이미지
        CommonImgResponseDto bgImage = studyBgImgService.findByStudyCode(studyCode);

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

        // 스터디 정보
        StudyDetailResponseDto studyResponseDto =
            StudyDetailResponseDto.builder()
                .code(study.getCode())
                .studyName(study.getStudyName())
                .categories(ListUtil.parseStringToList(study.getCategories()))
                .peopleCount(study.getPeopleCount())
                .joinCount(study.getJoinCount())
                .endPeriod(study.getEndPeriod())
                .dDay(DateUtil.calculateDDay(study.getEndPeriod()))
                .studyTitle(study.getStudyTitle())
                .summary(study.getSummary())
                .introduction(ListUtil.parseStringToList(study.getIntroduction()))
                .description(study.getDescription())
                .recommend(ListUtil.parseStringToList(study.getRecommend()))
                .bgImg(bgImage)
                .isJoined(isJoined)
                .isBookmark(isBookmark)
                .status(study.getStatus())
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
                .categories(ListUtil.parseStringToList(item.getCategories()))
                .dDay(DateUtil.calculateDDay(item.getEndPeriod()))
                .progress(0)
                .bgImg(studyBgImgService.findByStudyCode(item.getCode()))
                .isAdmin(Objects.equals(userCode, item.getUser().getCode()))
                .isBookmark(studyBookmarkRepository.findByUser_CodeAndStudy_Code(userCode, item.getCode()).isPresent())
                .status(item.getStatus())
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
                .categories(ListUtil.parseStringToList(study.getCategories()))
                .dDay(DateUtil.calculateDDay(study.getEndPeriod()))
                .progress(0)
                .bgImg(studyBgImgService.findByStudyCode(study.getCode()))
                .isAdmin(Objects.equals(userCode, study.getUser().getCode()))
                .isBookmark(studyBookmarkRepository.findByUser_CodeAndStudy_Code(userCode, study.getCode()).isPresent())
                .status(study.getStatus())
                .build())
            .toList();

        return responseDto;
    }

    @Transactional // 현재 진행 중인 스터디 조회 Service
    public List<StudyResponseDto> findStudying(Long userCode) {
        List<AsgmtCmt> commentList = asgmtCmtRepository.findStudying(userCode);

        return commentList.stream()
            .map(cmt -> cmt.getAsgmt().getStudy())
            .distinct() // 가장 먼저 나온(최신 댓글) 스터디만 유지
            .map(study -> StudyResponseDto.builder()
                .code(study.getCode())
                .studyName(study.getStudyName())
                .dDay(DateUtil.calculateDDay(study.getEndPeriod()))
                .progress(0)
                .bgImg(studyBgImgService.findByStudyCode(study.getCode()))
                .status(study.getStatus())
                .build())
            .toList();
    }

    @Transactional // 스터디 수정 Service
    public CommonStudyResponseDto updateStudy(Long studyCode, StudyRequestDto dto) {
        Study study = studyRepository.findById(studyCode)
            .orElseThrow(() -> new CustomException(ErrorCode.STUDY_NOT_FOUND));

        if (dto.getImgUrl() != null) {
            studyBgImgService.updateImgUrl(studyCode, dto.getImgUrl());
        }

        study.update(dto);

        return CommonStudyResponseDto.builder()
                .code(study.getCode())
                .userCode(study.getUser().getCode())
                .build();
    }

    @Transactional // 스터디 탈퇴 Service
    public void exitStudy(Long userCode, Long studyCode) {
        Study study = studyRepository.findById(studyCode)
            .orElseThrow(() -> new CustomException(ErrorCode.STUDY_NOT_FOUND));

        asgmtCmtService.deleteByUserCodeAndStudyCode(userCode, studyCode); // 내가 작성한 댓글 삭제
        studyJoinRepository.deleteByUser_CodeAndStudy_Code(userCode, studyCode); // DB 삭제
        study.decreaseJoinCount(); // 가입 인원수 감소
    }

    @Transactional // 스터디 삭제 Service
    public void deleteByStudyCode(Long studyCode) {
        asgmtService.deleteByStudyCode(studyCode); // 과제 삭제
        noticeService.deleteByStudyCode(studyCode); // 공지 삭제
        studyBgImgService.deleteByStudyCode(studyCode); // 스터디 배경 이미지 삭제
        studyViewService.deleteByStudyCode(studyCode); // 조회수 데이터 삭제
        studyJoinService.deleteByStudyCode(studyCode); // 가입 데이터 삭제
        studyBookmarkService.deleteByStudyCode(studyCode); // 북마크 데이터 삭제
        chatRoomService.deleteByStudyCode(studyCode); // 채팅방 삭제
        studyRepository.deleteById(studyCode); // 스터디 삭제
    }
}
