package com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.infrastructure;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class FileUtil {

    public String getFileExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf('.') + 1);
    }

    // return:
    //      key     |     value
    //    fileName  |    ten file da luu (VD: a.jpg)
    //    targetDir |   thu muc da luu file vao (VD: D:/hoctap/)
    //    fullPath  |   targetDir + fileName
    public Map<String, String> saveImageFile(MultipartFile file, String targetDir) throws IOException {
        String fileExtension = getFileExtension(file.getOriginalFilename());
        if (!fileExtension.equals("jpg")
                && !fileExtension.equals("png")) {
            throw new RuntimeException("Định dạng file không hợp lệ");
        }

        String newFileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path path = Paths.get(targetDir, newFileName);
        Files.write(path, file.getBytes());
        Map<String, String> data = new HashMap<>();
        data.put("fileName", newFileName);
        data.put("targetDir", targetDir);
        data.put("fullPath", targetDir + newFileName);
        return data;
    }
}
