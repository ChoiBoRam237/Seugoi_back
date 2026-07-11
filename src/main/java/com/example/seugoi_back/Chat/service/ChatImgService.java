package com.example.seugoi_back.Chat.service;

import com.example.seugoi_back.Chat.entity.ChatImg;
import com.example.seugoi_back.Chat.repository.ChatImgRepository;
import com.example.seugoi_back.Common.response.CommonImgResponseDto;
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
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatImgService {
    private final ChatImgRepository chatImgRepository;

    private final String UPLOAD_DIR = "D:\\Y2026\\Projects\\seugoi_back\\uploads\\chat";

    public List<String> savedChatImg(List<MultipartFile> fileList) { // 이미지 저장 Service
        if (fileList == null || fileList.isEmpty()) {
            throw new IllegalArgumentException("이미지를 업로드해 주세요");
        }

        if (fileList.size() > 3) {
            throw new IllegalArgumentException("이미지는 최대 3장까지 업로드 가능합니다.");
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

    @Transactional // 이미지 파일만 저장 Service
    public List<String> saveImgList(List<MultipartFile> imgList) {
        if (imgList == null || imgList.isEmpty()) {
            return Collections.emptyList();
        }

        return savedChatImg(imgList);
    }

    @Transactional // 채팅 메시지 code에 맞는 이미지 조회 Service
    public List<String> findByChatMessageCode(Long chatMessageCode) {
        List<ChatImg> imgList = chatImgRepository.findByChatMessage_Code(chatMessageCode);

        List<CommonImgResponseDto> responseDto = imgList.stream()
            .map(item -> CommonImgResponseDto.builder()
                .code(item.getCode())
                .folderName(item.getFolderName())
                .imgUrl(item.getImgUrl())
                .build()
            ).toList();

        if (responseDto.isEmpty()) {
            return Collections.emptyList();
        }

        return responseDto.stream().map(item -> item.getFolderName() + item.getImgUrl()).toList();
    }

    @Transactional // 채팅 메시지 code에 맞는 이미지 모두 삭제 Service
    public void deleteByChatMessageCode(Long chatMessageCode) {
        List<ChatImg> imgList = chatImgRepository.findByChatMessage_Code(chatMessageCode);

        for (ChatImg img : imgList) {
            FileUtil.deleteImg(img.getFolderName(), img.getImgUrl());
            chatImgRepository.deleteById(img.getCode());
        }
    }
}
