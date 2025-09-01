package com.repair_service.repairsystem.service;

import com.repair_service.repairsystem.entity.RepairRequest;
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

    /**
     * Zapisuje pojedynczy plik w tabeli uploaded_file powiązany z RepairRequest.
     *
     * @param request zgłoszenie naprawy
     * @param filePath ścieżka do pliku na dysku
     */
    void saveUploadedFile(RepairRequest request, String filePath);
}
