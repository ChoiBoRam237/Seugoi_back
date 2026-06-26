package com.example.seugoi_back.Study.service;

import com.example.seugoi_back.Study.dto.request.StudyAsgmtRequestDto;
import com.example.seugoi_back.Study.entity.Study;
import com.example.seugoi_back.Study.entity.StudyAsgmtImage;
import com.example.seugoi_back.Study.entity.StudyAsgmt;
import com.example.seugoi_back.Study.repository.StudyAsgmtImageRepository;
import com.example.seugoi_back.Study.repository.StudyAsgmtRepository;
import com.example.seugoi_back.Study.repository.StudyRepository;
import com.example.seugoi_back.User.entity.User;
import com.example.seugoi_back.User.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudyAsgmtService {
    private final UserRepository userRepository;
    private final StudyRepository studyRepository;
    private final StudyAsgmtRepository studyAsgmtRepository;
    private final StudyAsgmtImageRepository studyAsgmtImageRepository;
    private final StudyAsgmtImageService studyAsgmtImageService;

    ObjectMapper mapper = new ObjectMapper();

    @Transactional // 스터디 과제 생성 Service
    public StudyAsgmt generateStudyAsgmt(Long userCode, Long studyCode, StudyAsgmtRequestDto dto) {
        User user = userRepository.findById(userCode).orElseThrow();
        Study study = studyRepository.findById(studyCode).orElseThrow();

        // 스터디 과제 정보 저장
        StudyAsgmt studyAsgmt = StudyAsgmt.builder()
            .user(user)
            .study(study)
            .title(dto.getTitle())
            .content(dto.getContent())
            .linkName(dto.getLinkName())
            .linkUrl(dto.getLinkUrl())
            .build();
        StudyAsgmt savedAsgmt = studyAsgmtRepository.save(studyAsgmt);

        // 이미지가 있을 때만 실행
        if (dto.getImageList() != null && !dto.getImageList().isEmpty()) {
            // 스터디 과제 이미지 저장
            List<String> asgmtImageUrl = studyAsgmtImageService.savedAsgmtImage(dto.getImageList());
            StudyAsgmtImage studyAsgmtImage = StudyAsgmtImage.builder()
                .user(user)
                .study(study)
                .studyAsgmt(studyAsgmt)
                .imageUrlList(mapper.writeValueAsString(asgmtImageUrl))
                .build();
            studyAsgmtImageRepository.save(studyAsgmtImage);
        }

        return savedAsgmt;
    }
}
