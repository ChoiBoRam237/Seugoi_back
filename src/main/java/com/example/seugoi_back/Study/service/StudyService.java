package com.example.seugoi_back.Study.service;

import com.example.seugoi_back.Study.dto.request.StudyRequestDto;
import com.example.seugoi_back.Study.entity.Study;
import com.example.seugoi_back.Study.entity.StudyBgImage;
import com.example.seugoi_back.Study.repository.StudyBgImageRepository;
import com.example.seugoi_back.Study.repository.StudyRepository;
import com.example.seugoi_back.User.entity.User;
import com.example.seugoi_back.User.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

@Service
@RequiredArgsConstructor
public class StudyService {
    private final UserRepository userRepository;
    private final StudyRepository studyRepository;
    private final StudyBgImageRepository studyBgImageRepository;

    private final StudyBgImageService studyBgImageService;

    ObjectMapper mapper = new ObjectMapper();

    @Transactional
    public Study generateStudy(StudyRequestDto dto) {
        User user = userRepository.findById(dto.getUserId()).orElseThrow();
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
}
