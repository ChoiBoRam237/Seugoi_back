package com.example.seugoi_back.Study.service;

import com.example.seugoi_back.Common.response.CommonImgResponseDto;
import com.example.seugoi_back.Study.entity.StudyBgImg;
import com.example.seugoi_back.Study.repository.StudyBgImgRepository;
import com.example.seugoi_back.Util.FileUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StudyBgImgService {
    private final StudyBgImgRepository studyBgImageRepository;
    private final String UPLOAD_DIR = "D:\\2026년\\Projects\\seugoi_back\\uploads\\study";

    public String saveBgImage(MultipartFile file) { // 이미지 저장 Service
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("이미지를 업로드해주세요.");
        }

        try {
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String fileName = UUID.randomUUID() + extension;

            Path uploadPath = Paths.get(UPLOAD_DIR);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Path filePath = uploadPath.resolve(fileName);

            file.transferTo(filePath.toFile());

            return fileName;

        } catch (IOException e) {
            throw new RuntimeException("이미지 저장 실패", e);
        }
    }

    @Transactional // 스터디 id에 맞는 이미지 조회 Service
    public CommonImgResponseDto findByStudyCode(Long studyCode) {
        StudyBgImg studyBgImage = studyBgImageRepository.findByStudy_Code(studyCode)
                .orElseThrow(() -> new RuntimeException("이미지를 찾을 수 없습니다."));

        return CommonImgResponseDto.builder()
                .code(studyBgImage.getCode())
                .folderName(studyBgImage.getFolderName())
                .imgUrl(studyBgImage.getImgUrl())
                .build();
    }

    @Transactional // 스터디 code에 맞는 이미지 수정 Service
    public StudyBgImg updateImgUrl(Long studyCode, MultipartFile imgUrl) {
        StudyBgImg studyBgImg = studyBgImageRepository.findByStudy_Code(studyCode)
            .orElseThrow(() -> new RuntimeException("이미지를 찾을 수 없습니다."));

        if (imgUrl == null || imgUrl.isEmpty()) {
            return studyBgImg;
        }

        // 기존 이미지 삭제
        deleteByStudyCode(studyCode);

        // 새 이미지 저장
        String newImgUrl = saveBgImage(imgUrl);

        // DB 업데이트
        studyBgImg.update(newImgUrl);

        return studyBgImg;
    }

    @Transactional // 스터디 code에 맞는 이미지 삭제
    public void deleteByStudyCode(Long studyCode) {
        Optional<StudyBgImg> bgImg = studyBgImageRepository.findByStudy_Code(studyCode);
        FileUtil.deleteImg(bgImg.get().getFolderName(), bgImg.get().getImgUrl());
        studyBgImageRepository.deleteById(bgImg.get().getCode());
    }
}
