package com.example.seugoi_back.Study.service;

import com.example.seugoi_back.Study.dto.request.StudyRequestDto;
import com.example.seugoi_back.Study.dto.response.StudyDetailResponseDto;
import com.example.seugoi_back.Study.dto.response.StudyResponseDto;
import com.example.seugoi_back.Study.entity.Study;
import com.example.seugoi_back.Study.entity.StudyBgImage;
import com.example.seugoi_back.Study.repository.StudyBgImageRepository;
import com.example.seugoi_back.Study.repository.StudyRepository;
import com.example.seugoi_back.User.entity.User;
import com.example.seugoi_back.User.repository.UserRepository;
import com.example.seugoi_back.Util.DateUtil;
import com.example.seugoi_back.Util.ListUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StudyService {
    private final UserRepository userRepository;
    private final StudyRepository studyRepository;
    private final StudyBgImageRepository studyBgImageRepository;

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
    public List<StudyResponseDto> findStudyAll() {
        List<Study> studyList = studyRepository.findAll();

        List<StudyResponseDto> responseDto = studyList.stream()
            .map(item -> StudyResponseDto.builder()
                .adminCode(item.getUser().getId())
                .studyId(item.getId())
                .studyName(item.getStudyName())
                .dDay(DateUtil.calculateDDay(item.getEndPeriod()))
                .progress(0)
                .bgImageUrl(studyBgImageService.findBgImageById(item.getId()).getStudyBgImgUrl())
                .build())
            .toList();

        return responseDto;
    }

    // 특정 스터디 조회 Service
    @Transactional
    public Map<String, Object> findStudyById(Long studyId) {
        Study study = studyRepository.findById(studyId)
            .orElseThrow(() -> new RuntimeException("스터디를 찾을 수 없습니다."));

        StudyBgImage bgImage = studyBgImageService.findBgImageById(studyId);

        User adminResponseDto = User.builder()
            .nickname(study.getUser().getNickname())
            .profileImageUrl(study.getUser().getProfileImageUrl())
            .build();

        StudyDetailResponseDto studyResponseDto =
            StudyDetailResponseDto.builder()
                .studyId(study.getId())
                .studyName(study.getStudyName())
                .categories(ListUtil.parseStringList(study.getCategories()))
                .peopleCount(study.getPeopleCount())
                // TODO : 현재 가입한 인원 수 추가하기
                .dDay(DateUtil.calculateDDay(study.getEndPeriod()))
                .studyTitle(study.getStudyTitle())
                .summary(study.getSummary())
                .introduction(ListUtil.parseStringList(study.getIntroduction()))
                .description(study.getDescription())
                .recommend(ListUtil.parseStringList(study.getRecommend()))
                .bgImageUrl(bgImage.getStudyBgImgUrl())
                .build();

        return Map.of(
        "admin", adminResponseDto,
        "study", studyResponseDto
        );
    }
}
