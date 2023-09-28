package com.example.library.utils;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Component
public class ImageUpload {
    private final String UploadFolder = "D:\\java_Project\\Web_Project\\testprjytb\\Admin\\src\\main\\resources\\static\\img\\ImageProduct";

    public boolean uploadImage(MultipartFile imageProduct){
        boolean upload = false;
        try{
            Files.copy(imageProduct.getInputStream(),
                    Paths.get(UploadFolder + File.separator, imageProduct.getOriginalFilename()),
                    StandardCopyOption.REPLACE_EXISTING);

            upload = true;

        }catch (Exception e){
            e.printStackTrace();
        }
        return upload;
    }

    public boolean checkExisted(MultipartFile imageProduct){
        boolean existed = false;
        try{
            File file = new File(UploadFolder + "\\" + imageProduct.getOriginalFilename());
            existed = true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return existed;
    }
}
