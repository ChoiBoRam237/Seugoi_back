package com.example.seugoi_back.Study.service;

import com.example.seugoi_back.Study.entity.StudyBgImage;
import com.example.seugoi_back.Study.repository.StudyBgImageRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StudyBgImageService {
    private final StudyBgImageRepository studyBgImageRepository;
    private final String UPLOAD_DIR = "D:\\2026년\\Projects\\seugoi_back\\uploads\\study";
    private final String UPLOAD_FILE_DIR = "/uploads/study";

    // 이미지 저장 Service
    public String saveImage(MultipartFile file) {
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

            return UPLOAD_FILE_DIR + fileName;

        } catch (IOException e) {
            throw new RuntimeException("이미지 저장 실패", e);
        }
    }

    // 스터디 id에 맞는 이미지 조회 Service
    @Transactional
    public StudyBgImage findBgImageById(Long studyId) {
        StudyBgImage studyBgImage = studyBgImageRepository.findByStudyId(studyId)
                .orElseThrow(() -> new RuntimeException("이미지를 찾을 수 없습니다."));

        return studyBgImage;
    }
}
