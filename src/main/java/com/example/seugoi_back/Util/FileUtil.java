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

            if (Files.exists(path)) {
                Files.delete(path);
            } else {
                throw new RuntimeException("삭제할 파일이 존재하지 않습니다: " + path.toAbsolutePath());
            }
        } catch (IOException e) {
            throw new RuntimeException("이미지 삭제에 실패했습니다.", e);
        }
    }
}
