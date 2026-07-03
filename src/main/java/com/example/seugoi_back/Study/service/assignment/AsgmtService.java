package com.example.seugoi_back.Study.service.assignment;

import com.example.seugoi_back.Study.dto.request.assignment.AsgmtRequestDto;
import com.example.seugoi_back.Study.dto.response.CommonStudyResponseDto;
import com.example.seugoi_back.Study.dto.response.StudyBoardResponseDto;
import com.example.seugoi_back.Study.entity.Study;
import com.example.seugoi_back.Study.entity.assignment.AsgmtImg;
import com.example.seugoi_back.Study.entity.assignment.Asgmt;
import com.example.seugoi_back.Study.repository.assignment.AsgmtCmtRepository;
import com.example.seugoi_back.Study.repository.assignment.AsgmtImgRepository;
import com.example.seugoi_back.Study.repository.assignment.AsgmtRepository;
import com.example.seugoi_back.Study.repository.StudyRepository;
import com.example.seugoi_back.User.entity.User;
import com.example.seugoi_back.User.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AsgmtService {
    private final UserRepository userRepository;
    private final StudyRepository studyRepository;
    private final AsgmtRepository asgmtRepository;
    private final AsgmtImgRepository asgmtImgRepository;
    private final AsgmtCmtRepository asgmtCmtRepository;
    private final AsgmtImgService asgmtImgService;
    private final AsgmtCmtService asgmtCmtService;

    @Transactional // 스터디 과제 생성 Service
    public Asgmt generateAsgmt(Long userCode, Long studyCode, AsgmtRequestDto dto) {
        User user = userRepository.findById(userCode)
            .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));
        Study study = studyRepository.findById(studyCode)
            .orElseThrow(() -> new RuntimeException("스터디를 찾을 수 없습니다."));

        // 스터디 과제 정보 저장
        Asgmt asgmt = Asgmt.builder()
            .user(user)
            .study(study)
            .title(dto.getTitle())
            .content(dto.getContent())
            .linkName(dto.getLinkName() != null ? dto.getLinkName() : dto.getLinkUrl())
            .linkUrl(dto.getLinkUrl())
            .build();
        Asgmt savedAsgmt = asgmtRepository.save(asgmt);

        // 이미지가 있을 때만 실행
        if (dto.getImageList() != null && !dto.getImageList().isEmpty()) {
            // 스터디 과제 이미지 저장
            List<String> asgmtImageUrl = asgmtImgService.savedAsgmtImg(dto.getImageList());
            for (String img : asgmtImageUrl) {
                AsgmtImg asgmtImg = AsgmtImg.builder()
                    .user(user)
                    .asgmt(savedAsgmt)
                    .folderName("/uploads/study/asgmt/")
                    .imgUrl(img)
                    .build();
                asgmtImgRepository.save(asgmtImg);
            }
        }

        return savedAsgmt;
    }

    @Transactional // 스터디 code에 맞는 모든 과제 조회 Service
    public List<StudyBoardResponseDto> findAsgmtAll(Long userCode, Long studyCode) {
        List<Asgmt> asgmtList = asgmtRepository.findByStudy_Code(studyCode);

        List<StudyBoardResponseDto> responseDto = asgmtList.stream()
            .map(item -> StudyBoardResponseDto.builder()
                .code(item.getCode())
                .target("asgmt")
                .title(item.getTitle())
                .content(item.getContent())
                .linkName(item.getLinkName())
                .linkUrl(item.getLinkUrl())
                .imgList(asgmtImgService.findByAsgmtCode(item.getCode()))
                .isAdmin(Objects.equals(item.getUser().getCode(), userCode))
                .submitted(asgmtCmtRepository.existsByAsgmt_CodeAndUser_Code(item.getCode(), userCode))
                .notSubmitCount(
                    studyRepository.findById(item.getStudy().getCode()).orElseThrow().getJoinCount() == 0
                        ? -1
                        : (studyRepository.findById(item.getStudy().getCode())
                            .orElseThrow()
                            .getJoinCount()) - item.getSubmitCount()
                )
                .createdAt(item.getCreatedAt())
                .build())
            .toList();

        return responseDto;
    }

    @Transactional // 특정 과제 조회 Service
    public StudyBoardResponseDto findByAsgmtCode(Long userCode, Long asgmtCode) {
        Asgmt asgmt = asgmtRepository.findById(asgmtCode)
            .orElseThrow(() -> new RuntimeException("과제를 찾을 수 없습니다."));
        Study study = studyRepository.findById(asgmt.getStudy().getCode())
            .orElseThrow(() -> new RuntimeException("스터디를 찾을 수 없습니다."));

        StudyBoardResponseDto responseDto =
            StudyBoardResponseDto.builder()
                .code(asgmt.getCode())
                .studyCode(asgmt.getStudy().getCode())
                .title(asgmt.getTitle())
                .content(asgmt.getContent())
                .linkName(asgmt.getLinkName())
                .linkUrl(asgmt.getLinkUrl())
                .imgList(asgmtImgService.findByAsgmtCode(asgmt.getCode()))
                .isAdmin(Objects.equals(asgmt.getUser().getCode(), userCode))
                .submitted(asgmtCmtRepository.existsByAsgmt_CodeAndUser_Code(asgmt.getCode(), userCode))
                .notSubmitCount(study.getJoinCount() == 0 ? -1 : study.getJoinCount() - asgmt.getSubmitCount())
                .createdAt(asgmt.getCreatedAt())
                .build();

        return responseDto;
    }

    @Transactional // 과제 수정 Service
    public CommonStudyResponseDto updateAsgmt(Long asgmtCode, AsgmtRequestDto dto, List<Long> removeImgCodeList) {
        Asgmt asgmt = asgmtRepository.findById(asgmtCode)
            .orElseThrow(() -> new RuntimeException("과제를 찾을 수 없습니다."));

        if (
            (dto.getImageList() != null && !dto.getImageList().isEmpty()) ||
            (removeImgCodeList != null && !removeImgCodeList.isEmpty())
        ) {
            asgmtImgService.updateImgUrl(asgmtCode, dto.getImageList(), removeImgCodeList);
        }

        asgmt.update(dto);

        return CommonStudyResponseDto.builder()
                .code(asgmt.getCode())
                .userCode(asgmt.getUser().getCode())
                .studyCode(asgmt.getStudy().getCode())
                .build();
    }

    @Transactional // 과제 삭제 Service
    public void deleteByAsgmtCode(Long asgmtCode) {
        asgmtCmtService.deleteByAsgmtCode(asgmtCode); // 과제 댓글 삭제
        asgmtImgService.deleteByAsgmtCode(asgmtCode); // 이미지 삭제
        asgmtRepository.deleteById(asgmtCode); // 과제 삭제
    }

    @Transactional // 스터디 code에 해당하는 모든 과제 삭제 Service
    public void deleteByStudyCode(Long studyCode) {
        // 댓글 삭제
        asgmtCmtService.deleteByStudyCode(studyCode);
        // 1. 스터디 code에 맞는 과제 목록 조회 후
        // 2. 과제 code에 맞는 이미지 삭제
        List<Asgmt> asgmtList = asgmtRepository.findByStudy_Code(studyCode);
        asgmtList.forEach(item -> asgmtImgService.deleteByAsgmtCode(item.getCode()));
        // 과제 삭제
        asgmtRepository.deleteByStudy_Code(studyCode);
    }
}
