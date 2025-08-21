package com.repair_service.repairsystem.service.impl;

import com.repair_service.repairsystem.entity.DeviceModel;
import com.repair_service.repairsystem.entity.RepairRequest;
import com.repair_service.repairsystem.entity.User;
import com.repair_service.repairsystem.repository.DeviceModelRepository;
import com.repair_service.repairsystem.repository.RepairRequestRepository;
import com.repair_service.repairsystem.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class RepairServiceImpl {

    private final RepairRequestRepository repairRequestRepository;
    private final DeviceModelRepository deviceModelRepository;
    private final UserRepository userRepository; // potrzebne do znalezienia klienta po emailu

    public RepairServiceImpl(RepairRequestRepository repairRequestRepository,
                             DeviceModelRepository deviceModelRepository,
                             UserRepository userRepository) {
        this.repairRequestRepository = repairRequestRepository;
        this.deviceModelRepository = deviceModelRepository;
        this.userRepository = userRepository;
    }

    public boolean createRepairRequest(String clientEmail, String deviceType, String description) {
        try {
            // Znajdź użytkownika po emailu
            User customer = userRepository.findByEmail(clientEmail)
                    .orElseThrow(() -> new RuntimeException("Client not found"));

            // Znajdź model urządzenia po nazwie
            DeviceModel model = deviceModelRepository.findByModelName(deviceType)
                    .orElseThrow(() -> new RuntimeException("Device model not found"));

            // Utwórz nowy rekord naprawy
            RepairRequest repairRequest = new RepairRequest();
            repairRequest.setCustomer(customer);      // ustaw klienta
            repairRequest.setModel(model);            // ustaw model urządzenia
            repairRequest.setDescription(description);
            repairRequest.setStatus("PENDING");       // przykładowy status
            repairRequestRepository.save(repairRequest);

            return true;
        } catch (Exception e) {
            e.printStackTrace(); // żeby widzieć błąd w logach
            return false;
        }
    }
}
