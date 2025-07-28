package com.repair_service.repairsystem.repository;

import com.repair_service.repairsystem.entity.RepairRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

//repozytorium do obsługi zgłoszeń napraw
public interface RepairRequestRepository extends JpaRepository<RepairRequest, Long>{

    // szukanie zgłoszenia po numerze śledzenia (trackingId)
    Optional<RepairRequest> findByTrackingId(String trackingId);
}
