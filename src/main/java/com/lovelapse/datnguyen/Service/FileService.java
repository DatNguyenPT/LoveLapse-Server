package com.lovelapse.datnguyen.Service;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Map;

@Service
public class FileService {
    private final Cloudinary cloudinary;

    public FileService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public Map uploadFile(MultipartFile file, String folderName, String fileName) throws IOException {
        return cloudinary.uploader().upload(file.getBytes(),
                ObjectUtils.asMap(
                        "folder", folderName,
                        "public_id", fileName
                ));
    }
    public Map uploadVideo(MultipartFile file, String folderName) throws IOException {
        return cloudinary.uploader().upload(file.getBytes(),
                ObjectUtils.asMap(
                        "resource_type", "video",
                        "folder", folderName
                ));
    }

    public boolean isFolderExists(String folderPath) {
        try {
            Map result = cloudinary.api().resources(ObjectUtils.asMap(
                    "type", "upload",
                    "prefix", folderPath,
                    "max_results", 1
            ));
            return !((Map) result.get("resources")).isEmpty();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // For user, folderPath = Users/username/imageName
    public String getFileURL(String folderPath, String fileName) {
        try {
            Map result = cloudinary.api().resource(folderPath + "/" + fileName, ObjectUtils.emptyMap());
            return (String) result.get("url");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getImageUrlWithTransformation(String folderPath, String imageName, Transformation transformation) {
        try {
            Map result = cloudinary.api().resource(folderPath + "/" + imageName,
                    ObjectUtils.asMap("transformation", transformation));
            return (String) result.get("url");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
