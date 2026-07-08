package com.example.seugoi_back.Util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtil {
    // 파일 삭제 util
    public static void deleteImg(String folderName, String imgUrl) {
        try {
            Path path = Paths.get(System.getProperty("user.dir"), folderName, imgUrl);
            Files.deleteIfExists(path);
        } catch (IOException e) {
            throw new RuntimeException("이미지 삭제에 실패했습니다.", e);
        }
    }
}
