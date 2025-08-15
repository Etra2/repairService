package com.repair_service.repairsystem.service.impl;

import com.repair_service.repairsystem.entity.RepairRequest;
import com.repair_service.repairsystem.repository.RepairRequestRepository;
import com.repair_service.repairsystem.service.EmailService;
import com.repair_service.repairsystem.service.RepairRequestService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RepairRequestServiceImpl implements RepairRequestService {

    private final RepairRequestRepository repairRequestRepository;
    private final EmailService emailService;

    // Wstrzykiwanie repozytorium i serwisu e-mail przez konstruktor
    public RepairRequestServiceImpl(RepairRequestRepository repairRequestRepository, EmailService emailService) {
        this.repairRequestRepository = repairRequestRepository;
        this.emailService = emailService;
    }

    @Override
    public RepairRequest createRepairRequest(RepairRequest request) {
        // generowanie unikalnego numeru śledzenia
        request.setTrackingId(UUID.randomUUID().toString());

        // ustawianie domyślnego statusu naprawy
        request.setStatus("OCZEKUJĄCE");

        // zapis do bazy
        return repairRequestRepository.save(request);
    }

    @Override
    public RepairRequest getRequestByTrackingId(String trackingId) {
        return repairRequestRepository
                .findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("Brak zgłoszenia o podanym ID"));
    }

    /**
     * Zakończenie naprawy i wysłanie powiadomienia e-mail
     * @param requestId - ID zgłoszenia
     * @return zaktualizowane zgłoszenie
     */
    @Override
    public RepairRequest completeRepair(Long requestId) {
        // pobranie zgłoszenia
        RepairRequest request = repairRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono zgłoszenia"));

        // aktualizacja statusu
        request.setStatus("ZAKOŃCZONE");
        RepairRequest updated = repairRequestRepository.save(request);

        // wysyłka powiadomienia e-mail
        emailService.sendEmail(
                request.getCustomer().getEmail(),
                "Naprawa zakończona",
                "Twoje zgłoszenie o numerze śledzenia " + request.getTrackingId() + " zostało zakończone."
        );

        return updated;
    }
}
