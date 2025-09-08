package com.repair_service.repairsystem.repository;

import com.repair_service.repairsystem.entity.RepairRequest;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RepairRequestRepository extends JpaRepository<RepairRequest, Long> {

    // Szukanie zgłoszenia po numerze śledzenia (trackingId)
    Optional<RepairRequest> findByTrackingId(String trackingId);

    // Szukanie wszystkich zgłoszeń danego klienta po emailu
    List<RepairRequest> findByCustomerEmail(String email);

    // Pobranie zgłoszenia wraz ze wszystkimi powiązaniami (fetch join) – nowa poprawna metoda
    @EntityGraph(attributePaths = {"customer","model","report","report.technician","uploadedFiles"})
    @Query("SELECT r FROM RepairRequest r WHERE r.id = :id")
    Optional<RepairRequest> findByIdWithAssociations(@Param("id") Long id);
}
