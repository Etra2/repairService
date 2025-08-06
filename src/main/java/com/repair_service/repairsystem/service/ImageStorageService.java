package com.repair_service.repairsystem.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageStorageService {
    /**
     * Zapisuje maksymalnie 3 zdjęcia dla zgłoszenia naprawy.
     *
     * @param repairRequestId ID zgłoszenia naprawy
     * @param images tablica plików (MultipartFile)
     * @return lista ścieżek do zapisanych plików
     */
    List<String> storeImages(Long repairRequestId, MultipartFile[] images);
}
