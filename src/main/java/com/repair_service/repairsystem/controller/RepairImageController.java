package com.repair_service.repairsystem.controller;

import com.repair_service.repairsystem.entity.RepairRequest;
import com.repair_service.repairsystem.repository.RepairRequestRepository;
import com.repair_service.repairsystem.service.ImageStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/repairs")
public class RepairImageController {

    private final RepairRequestRepository repairRequestRepository;
    private final ImageStorageService imageStorageService;

    @Autowired
    public RepairImageController(RepairRequestRepository repairRequestRepository, ImageStorageService imageStorageService) {
        this.repairRequestRepository = repairRequestRepository;
        this.imageStorageService = imageStorageService;
    }

    @PostMapping("/{id}/upload-images")
    public ResponseEntity<String> uploadImages(@PathVariable Long id, @RequestParam("images") MultipartFile[] images) {

        if (images.length > 3) {
            return ResponseEntity.badRequest().body("Możesz wgrać maksymalnie 3 zdjęcia.");
        }

        // pobranie zgłoszenia
        RepairRequest request = repairRequestRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Nie znaleziono zgłoszenia"));

        // zapis zdjęć na dysku
        List<String> imagePaths = imageStorageService.storeImages(id, images);

        // zapis ścieżek w tabeli uploaded_file
        imagePaths.forEach(path -> imageStorageService.saveUploadedFile(request, path));

        return ResponseEntity.ok("Zdjęcia zostały zapisane.");
    }
}
