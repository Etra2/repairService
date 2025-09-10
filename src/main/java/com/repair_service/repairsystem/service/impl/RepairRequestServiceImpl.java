package com.repair_service.repairsystem.service.impl;

import com.repair_service.repairsystem.entity.RepairRequest;
import com.repair_service.repairsystem.entity.User;
import com.repair_service.repairsystem.repository.RepairRequestRepository;
import com.repair_service.repairsystem.repository.UserRepository;
import com.repair_service.repairsystem.service.RepairRequestService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class RepairRequestServiceImpl implements RepairRequestService {

    private final RepairRequestRepository repairRequestRepository;
    private final UserRepository userRepository;

    public RepairRequestServiceImpl(RepairRequestRepository repairRequestRepository, UserRepository userRepository) {
        this.repairRequestRepository = repairRequestRepository;
        this.userRepository = userRepository;
    }

    @Override
    public RepairRequest createRepairRequest(RepairRequest repairRequest, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Użytkownik nie istnieje"));

        repairRequest.setCustomer(user);
        repairRequest.setTrackingId(UUID.randomUUID().toString());
        repairRequest.setStatus("NEW");

        return repairRequestRepository.save(repairRequest);
    }

    @Override
    public List<RepairRequest> getRepairsByCustomer(String userEmail) {
        return repairRequestRepository.findByCustomerEmail(userEmail);
    }

    @Override
    public RepairRequest getRepairById(Long id) {
        return repairRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono zgłoszenia"));
    }

    @Override
    public RepairRequest updateRepairStatus(Long id, String status) {
        RepairRequest repair = getRepairById(id);
        repair.setStatus(status);
        return repairRequestRepository.save(repair);
    }

    @Override
    public RepairRequest getRepairByTrackingId(String trackingId) {
        return repairRequestRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono zgłoszenia"));
    }

    @Override
    public List<RepairRequest> getAllRepairs() {
        // pobiera wszystkie zgłoszenia z bazy - dla technika
        return repairRequestRepository.findAll();
    }
}
