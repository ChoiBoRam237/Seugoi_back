package com.example.seugoi_back.Study.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class StudyBgImageService {
    private final String UPLOAD_DIR = "uploads/study/";

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

            return "/uploads/study/" + fileName;

        } catch (IOException e) {
            throw new RuntimeException("이미지 저장 실패", e);
        }
    }
}
