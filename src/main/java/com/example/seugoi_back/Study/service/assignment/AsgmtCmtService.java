package com.example.seugoi_back.Study.service.assignment;

import com.example.seugoi_back.Login.dto.UserResponseDto;
import com.example.seugoi_back.Study.dto.request.assignment.AsgmtCmtRequestDto;
import com.example.seugoi_back.Study.dto.response.CommonStudyResponseDto;
import com.example.seugoi_back.Study.dto.response.assignment.AsgmtCmtListResponseDto;
import com.example.seugoi_back.Study.dto.response.assignment.AsgmtCmtResponseDto;
import com.example.seugoi_back.Study.entity.assignment.Asgmt;
import com.example.seugoi_back.Study.entity.assignment.AsgmtCmt;
import com.example.seugoi_back.Study.entity.assignment.AsgmtCmtImg;
import com.example.seugoi_back.Study.repository.assignment.AsgmtCmtImgRepository;
import com.example.seugoi_back.Study.repository.assignment.AsgmtCmtRepository;
import com.example.seugoi_back.Study.repository.assignment.AsgmtRepository;
import com.example.seugoi_back.User.entity.User;
import com.example.seugoi_back.User.repository.UserRepository;
import com.example.seugoi_back.Util.ListUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AsgmtCmtService {
    private final UserRepository userRepository;
    private final AsgmtRepository asgmtRepository;
    private final AsgmtCmtRepository asgmtCmtRepository;
    private final AsgmtCmtImgRepository asgmtCmtImgRepository;
    private final AsgmtCmtImgService asgmtCmtImgService;

    @Transactional // 과제 댓글 생성 Service
    public AsgmtCmt generateAsgmtCmt(Long userCode, Long asgmtCode, AsgmtCmtRequestDto dto) {
        User user = userRepository.findById(userCode).orElseThrow();
        Asgmt asgmt = asgmtRepository.findById(asgmtCode).orElseThrow();

        // 과제 댓글 저장
        AsgmtCmt asgmtCmt = AsgmtCmt.builder()
            .user(user)
            .study(asgmt.getStudy())
            .asgmt(asgmt)
            .comment(dto.getComment())
            .build();
        AsgmtCmt savedCmt = asgmtCmtRepository.save(asgmtCmt);

        // 이미지가 있을 때만 실행
        if (dto.getImageList() != null && !dto.getImageList().isEmpty()) {
            // 과제 댓글 이미지 저장
            List<String> cmtImageUrl = asgmtCmtImgService.savedAsgmtCmtImg(dto.getImageList());
            for (String img : cmtImageUrl) {
                AsgmtCmtImg asgmtCmtImg = AsgmtCmtImg.builder()
                    .user(user)
                    .asgmtCmt(asgmtCmt)
                    .folderName("/uploads/study/asgmt/")
                    .imgUrl(img)
                    .build();
                asgmtCmtImgRepository.save(asgmtCmtImg);
            }
        }

        return savedCmt;
    }

    @Transactional // 과제 code에 맞는 모든 댓글 조회 Service
    public AsgmtCmtListResponseDto findByAsgmtCode(Long userCode, Long asgmtCode) {
        List<AsgmtCmt> myCmtList = asgmtCmtRepository.findByUser_CodeAndAsgmt_Code(userCode, asgmtCode);

        // 내가 제출한 과제가 없으면
        if (myCmtList.isEmpty()) {
            return AsgmtCmtListResponseDto.builder()
                    .submitted(false)
                    .comments(Collections.emptyList())
                    .build();
        }

        List<AsgmtCmt> asgmtCmtList = asgmtCmtRepository.findByAsgmt_Code(asgmtCode);

        List<AsgmtCmtResponseDto> responseDto = asgmtCmtList.stream()
            .map(item -> AsgmtCmtResponseDto.builder()
                .code(item.getCode())
                .comment(item.getComment())
                .imgList(asgmtCmtImgService.findByAsgmtCmtCode(item.getCode()))
                .isWriter(Objects.equals(item.getUser().getCode(), userCode))
                .createdAt(item.getCreatedAt())
                .user(
                    UserResponseDto.builder()
                        .userCode(item.getUser().getCode())
                        .name(item.getUser().getName())
                        .profileImgUrl(item.getUser().getProfileImgUrl())
                        .build()
                )
                .build())
            .toList();

        return AsgmtCmtListResponseDto.builder()
                .submitted(true)
                .comments(responseDto)
                .build();
    }

    @Transactional // 댓글 수정 Service
    public CommonStudyResponseDto updateAsgmtCmt(Long asgmtCmtCode, AsgmtCmtRequestDto dto, List<Long> removeImgCodeList) {
        AsgmtCmt asgmtCmt = asgmtCmtRepository.findById(asgmtCmtCode).orElseThrow();

        if (
            (dto.getImageList() != null && !dto.getImageList().isEmpty()) ||
            (removeImgCodeList != null && !removeImgCodeList.isEmpty())
        ) {
            asgmtCmtImgService.updateImgUrl(asgmtCmtCode, dto.getImageList(), removeImgCodeList);
        }

        asgmtCmt.update(dto);

        return CommonStudyResponseDto.builder()
                .code(asgmtCmt.getCode())
                .userCode(asgmtCmt.getUser().getCode())
                .studyCode(asgmtCmt.getStudy().getCode())
                .build();
    }

    @Transactional // 댓글 code에 맞는 댓글 삭제 Service
    public void deleteByAsgmtCmtCode(Long asgmtCmtCode) {
        asgmtCmtImgService.deleteByAsgmtCmtCode(asgmtCmtCode);
        asgmtCmtRepository.deleteById(asgmtCmtCode);
    }

    @Transactional // 과제 code에 맞는 댓글 삭제 Service
    public void deleteByAsgmtCode(Long asgmtCode) {
        // 1. 스터디 code에 맞는 과제 댓글 목록 조회 후
        // 2. 과제 댓글 code로 이미지 삭제
        List<AsgmtCmt> asgmtCmtList = asgmtCmtRepository.findByAsgmt_Code(asgmtCode);
        asgmtCmtList.forEach(item -> asgmtCmtImgService.deleteByAsgmtCmtCode(item.getCode()));
        asgmtCmtRepository.deleteByAsgmt_Code(asgmtCode);
    }

    @Transactional // 스터디 code에 맞는 댓글 삭제 Service
    public void deleteByStudyCode(Long studyCode) {
        // 1. 스터디 code에 맞는 과제 목록 조회
        // 2. 과제 code에 맞는 과제 댓글 목록 조회
        // 3. 과제 댓글 code로 이미지 삭제
        List<Asgmt> asgmtList = asgmtRepository.findByStudy_Code(studyCode);
        List<AsgmtCmt> asgmtCmtList = asgmtList.stream()
            .flatMap(asgmt -> asgmtCmtRepository.findByAsgmt_Code(asgmt.getCode()).stream())
            .toList();
        asgmtCmtList.forEach(asgmtCmt -> asgmtCmtImgService.deleteByAsgmtCmtCode(asgmtCmt.getCode()));
        // 과제 댓글 삭제
        asgmtCmtRepository.deleteByStudy_Code(studyCode);
    }
}
