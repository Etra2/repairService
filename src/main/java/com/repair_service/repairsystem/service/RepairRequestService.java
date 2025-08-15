package com.repair_service.repairsystem.service;

import com.repair_service.repairsystem.entity.RepairRequest;

public interface RepairRequestService {

    // Tworzenie nowego zgłoszenia naprawy
    RepairRequest createRepairRequest(RepairRequest request);

    // Pobieranie zgłoszenia po trackingId
    RepairRequest getRequestByTrackingId(String trackingId);

    // Zakończenie naprawy i wysłanie powiadomienia e-mail
    RepairRequest completeRepair(Long requestId);
}
