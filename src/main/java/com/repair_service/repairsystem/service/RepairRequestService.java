package com.repair_service.repairsystem.service;

import com.repair_service.repairsystem.entity.RepairRequest;

public interface RepairRequestService {

    // Tworzenie nowego zgłoszenia
    RepairRequest createRepairRequest(RepairRequest repairRequest, String userEmail);

    // Pobranie wszystkich zgłoszeń danego klienta
    java.util.List<RepairRequest> getRepairsByCustomer(String userEmail);

    // Pobranie zgłoszenia po ID
    RepairRequest getRepairById(Long id);

    // Aktualizacja statusu naprawy (dla technika)
    RepairRequest updateRepairStatus(Long id, String status);

    // śledzenie po id
    RepairRequest getRepairByTrackingId(String trackingId);
}
