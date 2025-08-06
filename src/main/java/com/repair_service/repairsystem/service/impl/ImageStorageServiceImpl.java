package com.repair_service.repairsystem.service.impl;


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

    // Ścieżka do katalogu, w którym będą zapisywane zdjęcia (np. "uploads/")
    @Value("${upload.path}")
    private String uploadDir;

    // Metoda zapisuje maksymalnie 3 zdjęcia powiązane z danym zgłoszeniem naprawy.
    @Override
    public List<String> storeImages(Long repairRequestId, MultipartFile[] images) {
        List<String> paths = new ArrayList<>();
        // sprawdzam czy katalog uploadu istnieje – jeśli nie, utwórz go
        File dir = new File(uploadDir);
        if (!dir.exists()) dir.mkdirs();

        int count = 0;
        for (MultipartFile image : images) {
            // sprawdzanie czy plik nie jest pusty i czy nie zapisano już 3 zdjęć
            if (!image.isEmpty() && count < 3) {
                //generowanie unikatowej nazwy pliku
                String filename = "repair_" + repairRequestId + "_img" + (count + 1) + "_" + UUID.randomUUID() + ".jpg";

                //tworzenie nowego pliku w folderze uploadów
                File dest = new File(uploadDir + File.separator + filename);

                try {
                   // zapisz zawartość przełanego pliku na dysku
                   image.transferTo(dest);

                   // dodaj pełną ścieżkę pliku do list
                   paths.add(dest.getAbsolutePath());

                   //zwiekszanie licznika zapisanych zdjęć
                    count++;
                } catch (IOException e) {
                    //obsługa błędu zapisu - zgłoś wyjątek z opisem
                    throw new RuntimeException("Błąd podczas zapisu pliku: " + filename, e);
                }
            }
        }
        return paths;
    }
}
