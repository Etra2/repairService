package com.repair_service.repairsystem.service.impl;

import com.repair_service.repairsystem.entity.RepairRequest;
import com.repair_service.repairsystem.repository.RepairRequestRepository;
import com.repair_service.repairsystem.service.RepairRequestService;
import org.springframework.stereotype.Service;

import java.util.UUID;

public class RepairRequestServiceImpl implements RepairRequestService {

    private final RepairRequestRepository repairRequestRepository;

    //wstrzykiwanie repozytorium przez konstruktor
    public RepairRequestServiceImpl(RepairRequestRepository repairRequestRepository) {
        this.repairRequestRepository = repairRequestRepository;
    }

    @Override
    public RepairRequest createRepairRequest(RepairRequest request) {
        // generowanie unikalnego numeru śledzenia
        request.setTrackingId(UUID.randomUUID().toString());

        //ustawianie domyślnego statusu naprawy
        request.setStatus("OCZEKUJĄCE");

        //zapisywanie zgłoszenia do bazy
        return repairRequestRepository.save(request);
    }

    @Override
    public RepairRequest getRequestByTrackingId(String trackingId) {
        return repairRequestRepository
                .findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("Brak zgłoszenia o podanym ID"));
    }
}
