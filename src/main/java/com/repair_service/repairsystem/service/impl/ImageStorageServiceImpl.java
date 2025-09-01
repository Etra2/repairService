package com.repair_service.repairsystem.service.impl;

import com.repair_service.repairsystem.entity.RepairRequest;
import com.repair_service.repairsystem.entity.UploadedFile;
import com.repair_service.repairsystem.repository.UploadedFileRepository;
import com.repair_service.repairsystem.service.ImageStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ImageStorageServiceImpl implements ImageStorageService {

    @Value("${upload.path}")
    private String uploadDir;

    private final UploadedFileRepository uploadedFileRepository;

    public ImageStorageServiceImpl(UploadedFileRepository uploadedFileRepository) {
        this.uploadedFileRepository = uploadedFileRepository;
    }

    // zapis zdjęć na dysku i zwrócenie ścieżek
    public List<String> storeImages(Long repairRequestId, MultipartFile[] images) {
        List<String> paths = new ArrayList<>();
        File dir = new File(uploadDir);
        if (!dir.exists()) dir.mkdirs();

        int count = 0;
        for (MultipartFile image : images) {
            if (!image.isEmpty() && count < 3) {
                String filename = "repair_" + repairRequestId + "_img" + (count + 1) + "_" + UUID.randomUUID() + ".jpg";
                File dest = new File(uploadDir + File.separator + filename);
                try {
                    image.transferTo(dest);
                    paths.add(dest.getAbsolutePath());
                    count++;
                } catch (IOException e) {
                    throw new RuntimeException("Błąd podczas zapisu pliku: " + filename, e);
                }
            }
        }
        return paths;
    }

    // zapis pojedynczego pliku w tabeli uploaded_file
    public void saveUploadedFile(RepairRequest request, String filePath) {
        UploadedFile uploadedFile = new UploadedFile();
        uploadedFile.setRepairRequest(request); // <--- tu wywołujemy poprawną metodę z Twojej encji
        uploadedFile.setFilePath(filePath);
        uploadedFileRepository.save(uploadedFile);
    }
}
