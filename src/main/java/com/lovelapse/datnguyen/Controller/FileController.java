package com.lovelapse.datnguyen.Controller;

import com.lovelapse.datnguyen.Service.FileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@RestController
public class FileController {
    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }
    @PostMapping("/upload/client/file")
    public ResponseEntity<?> uploadClientsFile(@RequestParam("file") MultipartFile file,
                                         @RequestParam("user") String username,
                                         @RequestParam("fileName") String fileName) throws IOException, IOException {
        String folderName = "Users/" + username;
        return ResponseEntity.ok(fileService.uploadFile(file, folderName, fileName));
    }

    @PostMapping("/upload/admin/file")
    public ResponseEntity<?> uploadAdminFile(@RequestParam("file") MultipartFile file,
                                        @RequestParam("admin") String adminName,
                                        @RequestParam("fileName") String fileName) throws IOException, IOException {
        String folderName = "Admin/" + adminName;
        return ResponseEntity.ok(fileService.uploadFile(file, folderName, fileName));
    }

    /*@PostMapping("/upload/video")
    public ResponseEntity<?> uploadVideo(@RequestParam("file") MultipartFile file,
                                         @RequestParam("folder") String username) throws IOException {
        String folderName = "Users/" + username;
        return ResponseEntity.ok(fileService.uploadVideo(file, folderName));
    }*/

    @GetMapping("/folder/client/check")
    public boolean checkClientFolderExists(@RequestParam("folderName") String folderName) {
        String folderPath = "Users/" + folderName;
        return fileService.isFolderExists(folderPath);
    }

    @GetMapping("/get/client/files")
    public ResponseEntity<?> retrieveFiles(@RequestParam("folderPath") String folderPath, @RequestParam("fileName") String fileName){
        return ResponseEntity.ok(fileService.getFileURL(folderPath, fileName));
    }
}
