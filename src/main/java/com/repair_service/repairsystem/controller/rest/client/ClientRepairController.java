package com.repair_service.repairsystem.controller.rest.client;

import com.repair_service.repairsystem.controller.rest.mapper.RepairMapper;
import com.repair_service.repairsystem.dto.repair.CreateRepairRequestDto;
import com.repair_service.repairsystem.dto.repair.RepairRequestDto;
import com.repair_service.repairsystem.entity.DeviceModel;
import com.repair_service.repairsystem.entity.RepairRequest;
import com.repair_service.repairsystem.entity.User;
import com.repair_service.repairsystem.repository.DeviceModelRepository;
import com.repair_service.repairsystem.repository.UserRepository;
import com.repair_service.repairsystem.security.UserDetailsImpl;
import com.repair_service.repairsystem.service.RepairRequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * Kontroler REST dla klientów (ROLE_CLIENT).
 * Tutaj klient może:
 *  - zgłosić naprawę
 *  - sprawdzić status swojego zgłoszenia
 */
@RestController
@RequestMapping("/api/client/repairs")
public class ClientRepairController {

    private final RepairRequestService repairRequestService;
    private final UserRepository userRepository;
    private final DeviceModelRepository deviceModelRepository;

    public ClientRepairController(RepairRequestService repairRequestService,
                                  UserRepository userRepository,
                                  DeviceModelRepository deviceModelRepository) {
        this.repairRequestService = repairRequestService;
        this.userRepository = userRepository;
        this.deviceModelRepository = deviceModelRepository;
    }

    // Klient zgłasza nowe zgłoszenie
    @PostMapping
    public ResponseEntity<RepairRequestDto> createRepair(
            @RequestBody CreateRepairRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        User customer = userRepository.findByEmail(userDetails.getEmail())
                .orElseThrow(() -> new RuntimeException("Użytkownik nie istnieje"));

        DeviceModel model = deviceModelRepository.findByModelName(requestDto.getDeviceModelName())
                .orElseGet(() -> {
                    DeviceModel newModel = new DeviceModel();
                    newModel.setManufacturer(requestDto.getManufacturer());
                    newModel.setModelName(requestDto.getDeviceModelName());
                    newModel.setCategory(requestDto.getCategory());
                    return deviceModelRepository.save(newModel);
                });

        RepairRequest repairRequest = new RepairRequest();
        repairRequest.setDescription(requestDto.getDescription());
        repairRequest.setDeviceSerialNumber(requestDto.getDeviceSerialNumber());
        repairRequest.setModel(model);

        RepairRequest created = repairRequestService.createRepairRequest(repairRequest, userDetails.getEmail());
        return ResponseEntity.ok(RepairMapper.mapToDto(created));
    }

    // Klient sprawdza status zgłoszenia
    @GetMapping("/status/{trackingId}")
    public ResponseEntity<RepairRequestDto> getRepairStatus(@PathVariable String trackingId) {
        RepairRequest repair = repairRequestService.getRepairByTrackingId(trackingId);
        return ResponseEntity.ok(RepairMapper.mapToDto(repair));
    }
}
