package com.repair_service.repairsystem.controller.rest;

import com.repair_service.repairsystem.dto.repair.CreateRepairRequestDto;
import com.repair_service.repairsystem.dto.repair.RepairRequestDto;
import com.repair_service.repairsystem.entity.DeviceModel;
import com.repair_service.repairsystem.entity.RepairRequest;
import com.repair_service.repairsystem.entity.UploadedFile;
import com.repair_service.repairsystem.entity.User;
import com.repair_service.repairsystem.repository.DeviceModelRepository;
import com.repair_service.repairsystem.repository.UserRepository;
import com.repair_service.repairsystem.security.UserDetailsImpl;
import com.repair_service.repairsystem.service.RepairRequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/repairs")
public class RepairRequestController {

    private final RepairRequestService repairRequestService;
    private final UserRepository userRepository;
    private final DeviceModelRepository deviceModelRepository;

    public RepairRequestController(RepairRequestService repairRequestService,
                                   UserRepository userRepository,
                                   DeviceModelRepository deviceModelRepository) {
        this.repairRequestService = repairRequestService;
        this.userRepository = userRepository;
        this.deviceModelRepository = deviceModelRepository;
    }

    @PostMapping
    public ResponseEntity<RepairRequestDto> createRepair(@RequestBody CreateRepairRequestDto requestDto,
                                                         @AuthenticationPrincipal UserDetailsImpl userDetails) {
        // pobranie zalogowanego użytkownika
        User customer = userRepository.findByEmail(userDetails.getEmail())
                .orElseThrow(() -> new RuntimeException("Użytkownik nie istnieje"));

        // sprawdzenie czy model urządzenia istnieje
        DeviceModel model = deviceModelRepository.findByModelName(requestDto.getDeviceModelName())
                .orElseGet(() -> {
                    DeviceModel newModel = new DeviceModel();
                    newModel.setManufacturer(requestDto.getManufacturer());
                    newModel.setModelName(requestDto.getDeviceModelName());
                    newModel.setCategory(requestDto.getCategory());
                    return deviceModelRepository.save(newModel);
                });

        // utworzenie zgłoszenia
        RepairRequest repairRequest = new RepairRequest();
        repairRequest.setDescription(requestDto.getDescription());
        repairRequest.setDeviceSerialNumber(requestDto.getDeviceSerialNumber());
        repairRequest.setModel(model);

        RepairRequest created = repairRequestService.createRepairRequest(repairRequest, userDetails.getEmail());

        // mapowanie RepairRequest -> RepairRequestDto
        RepairRequestDto dto = mapToDto(created);

        return ResponseEntity.ok(dto);
    }

    private RepairRequestDto mapToDto(RepairRequest request) {
        RepairRequestDto dto = new RepairRequestDto();
        dto.setId(request.getId());
        dto.setTrackingId(request.getTrackingId());
        dto.setDeviceSerialNumber(request.getDeviceSerialNumber());
        dto.setDescription(request.getDescription());
        dto.setImagePath1(request.getImagePath1());
        dto.setImagePath2(request.getImagePath2());
        dto.setImagePath3(request.getImagePath3());
        dto.setStatus(request.getStatus());
        dto.setCreatedAt(request.getCreatedAt());
        dto.setCustomerId(request.getCustomer().getId());
        dto.setModelId(request.getModel().getId());
        dto.setReportId(request.getReport() != null ? request.getReport().getId() : null);

        List<Long> uploadedFileIds = request.getUploadedFiles() != null
                ? request.getUploadedFiles().stream().map(UploadedFile::getId).collect(Collectors.toList())
                : null;
        dto.setUploadedFileIds(uploadedFileIds);

        return dto;
    }
}
