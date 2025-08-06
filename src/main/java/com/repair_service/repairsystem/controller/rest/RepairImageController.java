package com.repair_service.repairsystem.controller.rest;

import com.repair_service.repairsystem.entity.RepairReport;
import com.repair_service.repairsystem.entity.RepairRequest;
import com.repair_service.repairsystem.repository.RepairReportRepository;
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
@RequestMapping("/api/repairs") // Główny URL prefix dla kontrolera
public class RepairImageController {

    private final RepairReportRepository repairReportRepository;
    private final RepairRequestRepository repairRequestRepository;
    private final ImageStorageService imageStorageService;

    //konstruktor
    @Autowired

    public RepairImageController(RepairReportRepository repairReportRepository, RepairRequestRepository repairRequestRepository, ImageStorageService imageStorageService) {
        this.repairReportRepository = repairReportRepository;
        this.repairRequestRepository = repairRequestRepository;
        this.imageStorageService = imageStorageService;
    }

    // endpoint do uploadu zdjęć powiązanych z danym zgłoszeniem naprawy
    @PostMapping("/{id}/upload-images")
    public ResponseEntity<String> uploadImages(@PathVariable Long id, @RequestParam("images") MultipartFile[] images) {

        //sprawdzenie czy liczba zdjęć nie przekracza 3
        if (images.length > 3) {
            return ResponseEntity.badRequest().body("Możesz wgrać maksymalnie 3 zdjęcia.");
        }

        // pobieranie zgłoszenia po ID z bazy danych
        RepairRequest request = repairRequestRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Nie znaleziono zgłoszenia"));


        // zapis zdjęc do systemu plików i pobieranie listy ścieżek
        List<String> imagePaths = imageStorageService.storeImages(id, images);

        // zapis ścieżek w encji RepairRequest
        if (imagePaths.size() > 0) request.setImagePath1(imagePaths.get(0));
        if (imagePaths.size() > 1) request.setImagePath2(imagePaths.get(1));
        if (imagePaths.size() > 2) request.setImagePath3(imagePaths.get(2));

        // Zapis zaktualizowanego zgłoszenia do bazy danych
        repairRequestRepository.save(request);

        return ResponseEntity.ok("Zdjęcia zostały zapisane.");
    }
}
