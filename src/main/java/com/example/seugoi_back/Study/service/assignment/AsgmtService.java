package com.example.seugoi_back.Study.service.assignment;

import com.example.seugoi_back.Study.dto.request.assignment.AsgmtRequestDto;
import com.example.seugoi_back.Study.dto.response.StudyBoardResponseDto;
import com.example.seugoi_back.Study.entity.Study;
import com.example.seugoi_back.Study.entity.assignment.AsgmtImg;
import com.example.seugoi_back.Study.entity.assignment.Asgmt;
import com.example.seugoi_back.Study.repository.assignment.AsgmtImgRepository;
import com.example.seugoi_back.Study.repository.assignment.AsgmtRepository;
import com.example.seugoi_back.Study.repository.StudyRepository;
import com.example.seugoi_back.User.entity.User;
import com.example.seugoi_back.User.repository.UserRepository;
import com.example.seugoi_back.Util.ListUtil;
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
    private final AsgmtImgService asgmtImgService;
    private final AsgmtCmtService asgmtCmtService;

    @Transactional // 스터디 과제 생성 Service
    public Asgmt generateAsgmt(Long userCode, Long studyCode, AsgmtRequestDto dto) {
        User user = userRepository.findById(userCode).orElseThrow();
        Study study = studyRepository.findById(studyCode).orElseThrow();

        // 스터디 과제 정보 저장
        Asgmt asgmt = Asgmt.builder()
            .user(user)
            .study(study)
            .title(dto.getTitle())
            .content(dto.getContent())
            .linkName(dto.getLinkName())
            .linkUrl(dto.getLinkUrl())
            .build();
        Asgmt savedAsgmt = asgmtRepository.save(asgmt);

        // 이미지가 있을 때만 실행
        if (dto.getImageList() != null && !dto.getImageList().isEmpty()) {
            // 스터디 과제 이미지 저장
            List<String> asgmtImageUrl = asgmtImgService.savedAsgmtImg(dto.getImageList());
            AsgmtImg asgmtImg = AsgmtImg.builder()
                .user(user)
                .asgmt(asgmt)
                .imgUrlList(ListUtil.parseListToString(asgmtImageUrl))
                .build();
            asgmtImgRepository.save(asgmtImg);
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
                .imageList(asgmtImgService.findByAsgmtCode(item.getCode())
                            .stream()
                            .flatMap(image -> ListUtil.parseStringToList(image.getImgUrlList()).stream())
                            .toList()
                )
                .isAdmin(Objects.equals(item.getUser().getCode(), userCode))
                .createdAt(item.getCreatedAt())
                .build())
            .toList();

        return responseDto;
    }

    @Transactional // 특정 과제 조회 Service
    public StudyBoardResponseDto findByAsgmtCode(Long userCode, Long asgmtCode) {
        Asgmt asgmt = asgmtRepository.findById(asgmtCode).orElseThrow();
        List<AsgmtImg> asgmtImage = asgmtImgService.findByAsgmtCode(asgmtCode);

        StudyBoardResponseDto responseDto =
            StudyBoardResponseDto.builder()
                .code(asgmt.getCode())
                .title(asgmt.getTitle())
                .content(asgmt.getContent())
                .linkName(asgmt.getLinkName())
                .linkUrl(asgmt.getLinkUrl())
                .imageList(
                    asgmtImage.stream()
                    .flatMap(image -> ListUtil.parseStringToList(image.getImgUrlList()).stream())
                    .toList()
                )
                .isAdmin(Objects.equals(asgmt.getUser().getCode(), userCode))
                .createdAt(asgmt.getCreatedAt())
                .build();

        return responseDto;
    }

    // TODO : 스터디 과제 수정 service

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
