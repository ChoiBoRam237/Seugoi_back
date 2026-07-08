package com.example.seugoi_back.Study.service.assignment;

import com.example.seugoi_back.Common.exception.CustomException;
import com.example.seugoi_back.Common.exception.ErrorCode;
import com.example.seugoi_back.Common.response.CommonImgResponseDto;
import com.example.seugoi_back.Study.entity.assignment.Asgmt;
import com.example.seugoi_back.Study.entity.assignment.AsgmtImg;
import com.example.seugoi_back.Study.repository.assignment.AsgmtImgRepository;
import com.example.seugoi_back.Study.repository.assignment.AsgmtRepository;
import com.example.seugoi_back.Util.FileUtil;
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
public class AsgmtImgService {
    private final AsgmtRepository asgmtRepository;
    private final AsgmtImgRepository asgmtImgRepository;
    private final String UPLOAD_DIR = "D:\\Y2026\\Projects\\seugoi_back\\uploads\\study\\asgmt";

    public List<String> savedAsgmtImg(List<MultipartFile> fileList) { // 이미지 저장 Service
        if (fileList == null || fileList.isEmpty()) {
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

                imageUrls.add(fileName);
            }

            return imageUrls;
        } catch (IOException e) {
            throw new RuntimeException("이미지 저장 실패", e);
        }
    }

    @Transactional // 과제 code에 맞는 이미지 조회 Service
    public List<CommonImgResponseDto> findByAsgmtCode(Long asgmtCode) {
        List<AsgmtImg> imgList = asgmtImgRepository.findByAsgmt_Code(asgmtCode);

        return imgList.stream()
                .map(item -> CommonImgResponseDto.builder()
                    .code(item.getCode())
                    .folderName(item.getFolderName())
                    .imgUrl(item.getImgUrl())
                    .build()
                ).toList();
    }

    @Transactional // 과제 code에 맞는 이미지 수정 Service
    public void updateImgUrl(Long asgmtCode, List<MultipartFile> imageList, List<Long> removeImgCodeList) {
        Asgmt asgmt = asgmtRepository.findById(asgmtCode)
            .orElseThrow(() -> new CustomException(ErrorCode.ASGMT_NOT_FOUND));

        // 지울 이미지가 있을 경우
        if (removeImgCodeList != null && !removeImgCodeList.isEmpty()) {
            for (Long imgCode : removeImgCodeList) {
                AsgmtImg img = asgmtImgRepository.findById(imgCode)
                    .orElseThrow(() -> new CustomException(ErrorCode.IMAGE_NOT_FOUND));
                FileUtil.deleteImg(img.getFolderName(), img.getImgUrl()); // 파일 삭제
                asgmtImgRepository.deleteById(imgCode);  // DB 삭제
            }
        }

        if (imageList != null && !imageList.isEmpty()) {
            // 새 이미지 저장
            List<String> newImgUrlList = savedAsgmtImg(imageList);

            // DB 저장
            for (String imgUrl : newImgUrlList) {
                AsgmtImg asgmtImg = AsgmtImg.builder()
                    .user(asgmt.getUser())
                    .asgmt(asgmt)
                    .folderName("/uploads/study/asgmt/")
                    .imgUrl(imgUrl)
                    .build();
                asgmtImgRepository.save(asgmtImg);
            }
        }
    }

    @Transactional // 과제 code에 맞는 이미지 모두 삭제 Service
    public void deleteByAsgmtCode(Long asgmtCode) {
        List<AsgmtImg> asgmtImg = asgmtImgRepository.findByAsgmt_Code(asgmtCode);

        for (AsgmtImg img : asgmtImg) {
            FileUtil.deleteImg(img.getFolderName(), img.getImgUrl());
            asgmtImgRepository.deleteById(img.getCode());
        }
    }
}
