package com.example.seugoi_back.Study.service.assignment;

import com.example.seugoi_back.Study.entity.assignment.AsgmtCmtImg;
import com.example.seugoi_back.Study.repository.assignment.AsgmtCmtImgRepository;
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
public class AsgmtCmtImgService {
    private final AsgmtCmtImgRepository asgmtCmtImgRepository;
    private final String UPLOAD_DIR = "D:\\2026년\\Projects\\seugoi_back\\uploads\\study\\asgmt\\cmt";
    private final String UPLOAD_FILE_DIR = "/uploads/study/asgmt/cmt/";

    public List<String> savedAsgmtCmtImg(List<MultipartFile> fileList) { // 이미지 저장 Service
        if (fileList == null || fileList.isEmpty()) {
            throw new IllegalArgumentException("이미지를 업로드해주세요.");
        }

        if (fileList.size() > 3) {
            throw new IllegalArgumentException("이미지는 최대 3개까지 업로드 가능합니다.");
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

    @Transactional // 과제 댓글 code에 맞는 이미지 조회 Service
    public List<AsgmtCmtImg> findByAsgmtCmtCode(Long asgmtCmtCode) {
        return asgmtCmtImgRepository.findByAsgmtCmt_Code(asgmtCmtCode);
    }

    @Transactional // 과제 댓글 code에 맞는 이미지 모두 삭제 Service
    public void deleteByAsgmtCmtCode(Long asgmtCmtCode) {
        asgmtCmtImgRepository.deleteByAsgmtCmt_Code(asgmtCmtCode);
    }
}
