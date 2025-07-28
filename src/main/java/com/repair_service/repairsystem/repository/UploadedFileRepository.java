package com.repair_service.repairsystem.repository;

import com.repair_service.repairsystem.entity.UploadedFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// repozytorium do zdjec przesyłanych przez klienta
public interface UploadedFileRepository extends JpaRepository<UploadedFile, Long> {

    //pobieranie wszytskich plików powiazanych z danym zgłoszeniem
    List<UploadedFile> findByRepairRequestId(Long repairRequestId);
}
