package com.example.seugoi_back.Study.service;

import com.example.seugoi_back.Study.entity.StudyAsgmtImage;
import com.example.seugoi_back.Study.repository.StudyAsgmtImageRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StudyAsgmtImageService {
    private final StudyAsgmtImageRepository studyAsgmtImageRepository;
    private final String UPLOAD_DIR = "D:\\2026년\\Projects\\seugoi_back\\uploads\\study\\asgmt";
    private final String UPLOAD_FILE_DIR = "/uploads/study/asgmt/";

    public List<String> savedAsgmtImage(List<MultipartFile> fileList) { // 이미지 저장 Service
        if (fileList == null ||fileList.isEmpty()) {
            throw new IllegalArgumentException("이미지를 업로드해주세요.");
        }

        if (fileList.size() > 5) {
            throw new IllegalArgumentException("이미지는 최대 5개까지 업로드 가능합니다.");
        }

        try {
            Path uploadPath = Paths.get(UPLOAD_DIR);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            List<String> imageUrls = new ArrayList<>();

            for (MultipartFile file : fileList) {
                if (file == null || file.isEmpty()) {
                    continue;
                }

                String originalFilename = file.getOriginalFilename();
                String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
                String fileName = UUID.randomUUID() + extension;

                Path filePath = uploadPath.resolve(fileName);

                file.transferTo(filePath.toFile());

                imageUrls.add(UPLOAD_FILE_DIR + fileName);
            }

            return imageUrls;
        } catch (IOException e) {
            throw new RuntimeException("이미지 저장 실패", e);
        }
    }

    @Transactional // 스터디 과제 code에 맞는 이미지 조회 Service
    public StudyAsgmtImage findAsgmtImageByCode(Long studyAsgmtCode) {
        StudyAsgmtImage studyAsgmtImage = studyAsgmtImageRepository.findByStudyAsgmt_Code(studyAsgmtCode)
                .orElseThrow(() -> new RuntimeException("이미지를 찾을 수 없습니다."));

        return studyAsgmtImage;
    }

    @Transactional // 스터디 과제 code에 맞는 이미지 모두 삭제 Service
    public void deleteImageByStudyAsgmtCode(Long studyAsgmtCode) {
        studyAsgmtImageRepository.deleteByStudyAsgmt_Code(studyAsgmtCode);
    }

    @Transactional // 스터디 code에 맞는 이미지 모두 삭제 Service
    public void deleteImageByStudyCode(Long studyCode) {
        studyAsgmtImageRepository.deleteByStudy_Code(studyCode);
    }
}
