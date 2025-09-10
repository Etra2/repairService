package com.repair_service.repairsystem.repository;

import com.repair_service.repairsystem.entity.RepairReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RepairReportRepository extends JpaRepository<RepairReport, Long> {
    // Dodane do wyszukiwania raportu po zgłoszeniu
    Optional<RepairReport> findByRepairRequestId(Long repairRequestId);
}

