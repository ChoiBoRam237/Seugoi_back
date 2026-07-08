package com.example.seugoi_back.Study.service.assignment;

import com.example.seugoi_back.Common.exception.CustomException;
import com.example.seugoi_back.Common.exception.ErrorCode;
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
        User user = userRepository.findById(userCode)
            .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        Asgmt asgmt = asgmtRepository.findById(asgmtCode)
            .orElseThrow(() -> new CustomException(ErrorCode.ASGMT_NOT_FOUND));
        List<AsgmtCmt> asgmtCmtList = asgmtCmtRepository.findByUser_CodeAndAsgmt_Code(userCode, asgmtCode);

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
                    .asgmtCmt(savedCmt)
                    .folderName("/uploads/study/asgmt/cmt/")
                    .imgUrl(img)
                    .build();
                asgmtCmtImgRepository.save(asgmtCmtImg);
            }
        }

        // 과제 처음 제출하는 사용자일 경우
        if (asgmtCmtList == null || asgmtCmtList.isEmpty()) {
            asgmt.increaseSubmitCount(); // 과제 제출한 인원수 증가
        }

        return savedCmt;
    }

    @Transactional // 과제 code에 맞는 모든 댓글 조회 Service
    public AsgmtCmtListResponseDto findByAsgmtCode(Long userCode, Long asgmtCode) {
        Asgmt asgmt = asgmtRepository.findById(asgmtCode)
            .orElseThrow(() -> new CustomException(ErrorCode.ASGMT_NOT_FOUND));
        List<AsgmtCmt> myCmtList = asgmtCmtRepository.findByUser_CodeAndAsgmt_Code(userCode, asgmtCode);
        List<AsgmtCmt> asgmtCmtList = asgmtCmtRepository.findByAsgmt_Code(asgmtCode);

        List<AsgmtCmtResponseDto> responseDto = asgmtCmtList.stream()
            .map(item -> AsgmtCmtResponseDto.builder()
                .code(item.getCode())
                .comment(item.getComment())
                .imgList(asgmtCmtImgService.findByAsgmtCmtCode(item.getCode()))
                .isWriter(Objects.equals(item.getUser().getCode(), userCode))
                .isAdminCheck(item.getIsAdminCheck())
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

        // 이 과제의 관리자일 경우
        if (Objects.equals(asgmt.getUser().getCode(), userCode)) {
            return AsgmtCmtListResponseDto.builder()
                    .submitted(true)
                    .comments(responseDto)
                    .build();
        }

        // 내가 제출한 과제가 없으면
        if (myCmtList.isEmpty()) {
            return AsgmtCmtListResponseDto.builder()
                    .submitted(false)
                    .comments(Collections.emptyList())
                    .build();
        }

        return AsgmtCmtListResponseDto.builder()
                .submitted(true)
                .comments(responseDto)
                .build();
    }

    @Transactional // 댓글 수정 Service
    public CommonStudyResponseDto updateAsgmtCmt(Long asgmtCmtCode, AsgmtCmtRequestDto dto, List<Long> removeImgCodeList) {
        AsgmtCmt asgmtCmt = asgmtCmtRepository.findById(asgmtCmtCode)
            .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));

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
        AsgmtCmt asgmtCmt = asgmtCmtRepository.findById(asgmtCmtCode)
            .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));

        Long asgmtCode = asgmtCmt.getAsgmt().getCode();
        Long userCode = asgmtCmt.getUser().getCode();

        asgmtCmtImgService.deleteByAsgmtCmtCode(asgmtCmtCode); // 이미지 삭제
        asgmtCmtRepository.delete(asgmtCmt); // 댓글 삭제

        // 같은 과제에 댓글이 더 없는 경우 -> 과제 제출한 인원수 감소
        if (!asgmtCmtRepository.existsByAsgmt_CodeAndUser_Code(asgmtCode, userCode)) {
            asgmtCmt.getAsgmt().decreaseSubmitCount();
        }
    }

    @Transactional // 과제 code에 맞는 댓글 삭제 Service
    public void deleteByAsgmtCode(Long asgmtCode) {
        Asgmt asgmt = asgmtRepository.findById(asgmtCode)
            .orElseThrow(() -> new CustomException(ErrorCode.ASGMT_NOT_FOUND));
        // 1. 스터디 code에 맞는 과제 댓글 목록 조회 후
        // 2. 과제 댓글 code로 이미지 삭제
        List<AsgmtCmt> asgmtCmtList = asgmtCmtRepository.findByAsgmt_Code(asgmtCode);
        asgmtCmtList.forEach(item -> asgmtCmtImgService.deleteByAsgmtCmtCode(item.getCode()));
        asgmtCmtRepository.deleteByAsgmt_Code(asgmtCode);
        asgmt.setSubmitCount(0L); // 댓글 모두 삭제했으므로 과제 제출 인원수 0으로 초기화
    }

    @Transactional // 스터디 code에 맞는 댓글 삭제 Service
    public void deleteByStudyCode(Long studyCode) {
        // 1. 스터디 code에 맞는 과제 목록 조회
        List<Asgmt> asgmtList = asgmtRepository.findByStudy_Code(studyCode);

        // 2. 스터디 code에 맞는 과제 댓글 목록 조회
        List<AsgmtCmt> asgmtCmtList = asgmtCmtRepository.findByStudy_Code(studyCode);

        // 3. 과제 댓글 code로 이미지 삭제
        asgmtCmtList.forEach(asgmtCmt -> asgmtCmtImgService.deleteByAsgmtCmtCode(asgmtCmt.getCode()));
        asgmtCmtRepository.deleteByStudy_Code(studyCode); // 과제 댓글 삭제
        asgmtList.forEach(asgmt -> asgmt.setSubmitCount(0L)); // 모든 과제의 제출 수 초기화
    }

    @Transactional // 사용자 code & 스터디 code에 맞는 댓글 삭제 Service
    public void deleteByUserCodeAndStudyCode(Long userCode, Long studyCode) {
        // 1. 사용자 code & 스터디 code에 맞는 댓글 목록 조회
        List<AsgmtCmt> asgmtCmtList = asgmtCmtRepository.findByUser_CodeAndStudy_Code(userCode, studyCode);

        // 2. 제출했던 과제 목록 조회 (중복 제거)
        List<Asgmt> submittedAsgmtList = asgmtCmtList.stream()
            .map(AsgmtCmt::getAsgmt)
            .distinct()
            .toList();

        // 2. 과제 댓글 code로 이미지 삭제
        asgmtCmtList.forEach(asgmtCmt -> asgmtCmtImgService.deleteByAsgmtCmtCode(asgmtCmt.getCode()));

        // 3. 사용자 code & 스터디 code에 맞는 댓글 삭제
        asgmtCmtRepository.deleteByUser_CodeAndStudy_Code(userCode, studyCode);

        // 과제 댓글 제출 수 업데이트
        submittedAsgmtList.forEach(Asgmt::decreaseSubmitCount);
    }

    @Transactional // 과제 댓글 확인 처리 (관리자용) Service
    public void submitAsgmtCmt(Long asgmtCmtCode) {
        AsgmtCmt asgmtCmt = asgmtCmtRepository.findById(asgmtCmtCode)
            .orElseThrow(() -> new CustomException(ErrorCode.ASGMT_NOT_FOUND));
        asgmtCmt.setIsAdminCheck(true); // 확인 처리
    }
}
