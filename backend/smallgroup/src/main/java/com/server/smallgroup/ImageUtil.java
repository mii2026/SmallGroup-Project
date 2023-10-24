package com.server.smallgroup;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

public class ImageUtil {
    public static String saveImage(Long id, String path, MultipartFile image) throws IOException {
        File folder = new File(path);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        String contentType = image.getContentType();
        if(contentType.contains("jpeg")){
            contentType = ".jpg";
        }else if(contentType.contains("png")){
            contentType = ".png";
        }

        String imagePath = folder + File.separator + String.valueOf(id) + contentType;
        File destination = new File(imagePath);
        image.transferTo(destination);
        return imagePath;
    }
}
