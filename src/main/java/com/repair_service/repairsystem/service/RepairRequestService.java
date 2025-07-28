package com.repair_service.repairsystem.service;

import com.repair_service.repairsystem.entity.RepairRequest;

public interface RepairRequestService {

    // tworzenie nowego zgłoszenia naprawy
    RepairRequest createRepairRequest(RepairRequest request);

    //pobieranie po trackingId - później moze
    RepairRequest getRequestByTrackingId(String trackingId);
}
