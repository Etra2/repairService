package com.repair_service.repairsystem.repository;

import com.repair_service.repairsystem.entity.RepairRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RepairRequestRepository extends JpaRepository<RepairRequest, Long> {

    // Szukanie zgłoszenia po numerze śledzenia (trackingId)
    Optional<RepairRequest> findByTrackingId(String trackingId);

    // Szukanie wszystkich zgłoszeń danego klienta po emailu
    List<RepairRequest> findByCustomerEmail(String email);
}

