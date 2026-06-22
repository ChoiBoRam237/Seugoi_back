package com.example.seugoi_back.Study.service;

import com.example.seugoi_back.Study.dto.request.StudyRequestDto;
import com.example.seugoi_back.Study.dto.response.StudyDetailResponseDto;
import com.example.seugoi_back.Study.dto.response.StudyResponseDto;
import com.example.seugoi_back.Study.entity.Study;
import com.example.seugoi_back.Study.entity.StudyBgImage;
import com.example.seugoi_back.Study.entity.StudyJoin;
import com.example.seugoi_back.Study.repository.StudyBgImageRepository;
import com.example.seugoi_back.Study.repository.StudyJoinRepository;
import com.example.seugoi_back.Study.repository.StudyRepository;
import com.example.seugoi_back.User.entity.User;
import com.example.seugoi_back.User.repository.UserRepository;
import com.example.seugoi_back.Util.DateUtil;
import com.example.seugoi_back.Util.ListUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StudyService {
    private final UserRepository userRepository;
    private final StudyRepository studyRepository;
    private final StudyBgImageRepository studyBgImageRepository;
    private final StudyJoinRepository studyJoinRepository;

    private final StudyBgImageService studyBgImageService;

    ObjectMapper mapper = new ObjectMapper();

    // 스터디 생성 Service
    @Transactional
    public Study generateStudy(StudyRequestDto dto) {
        User user = userRepository.findById(dto.getUserCode()).orElseThrow();
        // 배열 -> String
        String categoriesJson = mapper.writeValueAsString(dto.getCategories());
        String introductionJson = mapper.writeValueAsString(dto.getIntroduction());
        String recommendJson = mapper.writeValueAsString(dto.getRecommend());

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

        String studyBgImageUrl = studyBgImageService.saveImage(dto.getBgImageUrl());

        StudyBgImage studyBgImage = StudyBgImage.builder()
                .study(savedStudy)
                .user(user)
                .studyBgImgUrl(studyBgImageUrl)
                .build();
        studyBgImageRepository.save(studyBgImage);

        return savedStudy;
    }

    // 모든 스터디 조회 Service
    @Transactional
    public List<StudyResponseDto> findStudyAll(Long userCode, String filterValue, String sortValue) {
        List<Study> studyList;

        // 필터
        switch (filterValue.toUpperCase()) {
            case "MY_STUDY": studyList = studyRepository.findByUser_Code(userCode);
            break;

            case "JOINED": studyList = studyJoinRepository.findByUser_Code(userCode)
                                            .stream()
                                            .map(StudyJoin::getStudy)
                                            .toList();
            break;

            case "ALL":
            default: studyList = studyRepository.findAll();
            break;
        }

        // 정렬
        switch (sortValue.toUpperCase()) {
            case "NAME": studyList.stream()
                            .sorted(Comparator.comparing(Study::getStudyName)).toList();
            break;

            case "LATEST":
            default: studyList.stream()
                        .sorted(Comparator.comparing(Study::getCreatedAt)).toList();
            break;
        }

        List<StudyResponseDto> responseDto = studyList.stream()
            .map(item -> StudyResponseDto.builder()
                .adminCode(item.getUser().getCode())
                .studyCode(item.getCode())
                .studyName(item.getStudyName())
                .dDay(DateUtil.calculateDDay(item.getEndPeriod()))
                .progress(0)
                .bgImageUrl(studyBgImageService.findBgImageByCode(item.getCode()).getStudyBgImgUrl())
                .build())
            .toList();

        return responseDto;
    }

    // 특정 스터디 조회 Service
    @Transactional
    public Map<String, Object> findStudyByCode(Long userCode, Long studyCode) {
        Study study = studyRepository.findById(studyCode)
            .orElseThrow(() -> new RuntimeException("스터디를 찾을 수 없습니다."));

        // 배경 이미지
        StudyBgImage bgImage = studyBgImageService.findBgImageByCode(studyCode);

        // 현재 가입한 인원수
        List<StudyJoin> studyJoin = studyJoinRepository.findByStudy_Code(studyCode);

        // 내가 이 스터디에 가입했는지 안했는지
        boolean isJoined = studyJoinRepository
                .findByUser_CodeAndStudy_Code(userCode, studyCode)
                .isPresent();

        User adminResponseDto = User.builder()
            .nickname(study.getUser().getNickname())
            .profileImageUrl(study.getUser().getProfileImageUrl())
            .build();

        StudyDetailResponseDto studyResponseDto =
            StudyDetailResponseDto.builder()
                .studyCode(study.getCode())
                .studyName(study.getStudyName())
                .categories(ListUtil.parseStringList(study.getCategories()))
                .peopleCount(study.getPeopleCount())
                .joinCount(studyJoin.size())
                .dDay(DateUtil.calculateDDay(study.getEndPeriod()))
                .studyTitle(study.getStudyTitle())
                .summary(study.getSummary())
                .introduction(ListUtil.parseStringList(study.getIntroduction()))
                .description(study.getDescription())
                .recommend(ListUtil.parseStringList(study.getRecommend()))
                .bgImageUrl(bgImage.getStudyBgImgUrl())
                .isJoined(isJoined)
                .build();

        return Map.of(
        "admin", adminResponseDto,
        "study", studyResponseDto
        );
    }
}
