package com.example.seugoi_back.Study.service;

import com.example.seugoi_back.Study.dto.request.StudyAsgmtRequestDto;
import com.example.seugoi_back.Study.dto.response.StudyListResponseDto;
import com.example.seugoi_back.Study.entity.Study;
import com.example.seugoi_back.Study.entity.StudyAsgmtImage;
import com.example.seugoi_back.Study.entity.StudyAsgmt;
import com.example.seugoi_back.Study.repository.StudyAsgmtImageRepository;
import com.example.seugoi_back.Study.repository.StudyAsgmtRepository;
import com.example.seugoi_back.Study.repository.StudyRepository;
import com.example.seugoi_back.User.entity.User;
import com.example.seugoi_back.User.repository.UserRepository;
import com.example.seugoi_back.Util.ListUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Objects;

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
    public StudyAsgmt generateAsgmt(Long userCode, Long studyCode, StudyAsgmtRequestDto dto) {
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

    @Transactional // 스터디 code에 맞는 모든 과제 조회 Service
    public List<StudyListResponseDto> findAsgmtAll(Long userCode, Long studyCode) {
        List<StudyAsgmt> asgmtList = studyAsgmtRepository.findAsgmtByStudy_Code(studyCode);

        List<StudyListResponseDto> responseDto = asgmtList.stream()
            .map(item -> StudyListResponseDto.builder()
                .code(item.getCode())
                .title(item.getTitle())
                .content(item.getContent())
                .linkName(item.getLinkName())
                .linkUrl(item.getLinkUrl())
                .imageList(studyAsgmtImageService.findAsgmtImageByCode(item.getCode())
                            .stream()
                            .flatMap(image -> ListUtil.parseStringList(image.getImageUrlList()).stream())
                            .toList()
                )
                .isAdmin(Objects.equals(item.getUser().getCode(), userCode))
                .createdAt(item.getCreatedAt())
                .build())
            .toList();

        return responseDto;
    }

    @Transactional // 특정 과제 조회 Service
    public StudyListResponseDto findAsgmtByCode(Long userCode, Long studyAsgmtCode) {
        StudyAsgmt asgmt = studyAsgmtRepository.findById(studyAsgmtCode).orElseThrow();
        List<StudyAsgmtImage> asgmtImage = studyAsgmtImageService.findAsgmtImageByCode(studyAsgmtCode);

        StudyListResponseDto responseDto =
            StudyListResponseDto.builder()
                .code(asgmt.getCode())
                .title(asgmt.getTitle())
                .content(asgmt.getContent())
                .linkName(asgmt.getLinkName())
                .linkUrl(asgmt.getLinkUrl())
                .imageList(
                    asgmtImage.stream()
                    .flatMap(image -> ListUtil.parseStringList(image.getImageUrlList()).stream())
                    .toList()
                )
                .isAdmin(Objects.equals(asgmt.getUser().getCode(), userCode))
                .createdAt(asgmt.getCreatedAt())
                .build();

        return responseDto;
    }

    // TODO : 스터디 과제 수정 service

    @Transactional // 과제 삭제 Service
    public void deleteAsgmt(Long studyAsgmtCode) {
        studyAsgmtImageService.deleteImageByStudyAsgmtCode(studyAsgmtCode); // 이미지 삭제
        studyAsgmtRepository.deleteById(studyAsgmtCode); // 과제 삭제
    }

    @Transactional // 스터디 code에 해당하는 모든 과제 삭제 Service
    public void deleteAsgmtByStudyCode(Long studyCode) {
        studyAsgmtImageService.deleteImageByStudyCode(studyCode); // 이미지 삭제
        studyAsgmtRepository.deleteByStudy_Code(studyCode); // 과제 삭제
    }
}
