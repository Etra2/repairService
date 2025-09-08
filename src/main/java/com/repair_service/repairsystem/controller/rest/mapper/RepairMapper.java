package com.repair_service.repairsystem.controller.rest.mapper;

import com.repair_service.repairsystem.dto.auth.UserDTO;
import com.repair_service.repairsystem.dto.device.DeviceModelDto;
import com.repair_service.repairsystem.dto.repair.RepairRequestDto;
import com.repair_service.repairsystem.dto.repair.UploadedFileDto;
import com.repair_service.repairsystem.entity.RepairRequest;
import com.repair_service.repairsystem.entity.UploadedFile;

import java.util.List;
import java.util.stream.Collectors;

public class RepairMapper {

    public static RepairRequestDto mapToDto(RepairRequest request) {
        RepairRequestDto dto = new RepairRequestDto();

        // Podstawowe pola naprawy (Twój istniejący kod)
        dto.setId(request.getId());
        dto.setTrackingId(request.getTrackingId());
        dto.setDeviceSerialNumber(request.getDeviceSerialNumber());
        dto.setDescription(request.getDescription());
        dto.setImagePath1(request.getImagePath1());
        dto.setImagePath2(request.getImagePath2());
        dto.setImagePath3(request.getImagePath3());
        dto.setStatus(request.getStatus());
        dto.setCreatedAt(request.getCreatedAt());
        dto.setCustomerId(request.getCustomer() != null ? request.getCustomer().getId() : null);

        if (request.getModel() != null) {
            DeviceModelDto modelDto = new DeviceModelDto(
                    request.getModel().getId(),
                    request.getModel().getManufacturer(),
                    request.getModel().getModelName(),
                    request.getModel().getCategory(),
                    null
            );
            dto.setDeviceModel(modelDto);
        }

        dto.setReportId(request.getReport() != null ? request.getReport().getId() : null);

        List<Long> uploadedFileIds = request.getUploadedFiles() != null
                ? request.getUploadedFiles().stream().map(UploadedFile::getId).collect(Collectors.toList())
                : null;
        dto.setUploadedFileIds(uploadedFileIds);

        // --------- nowe pola dla modala ----------
        if (request.getCustomer() != null) {
            UserDTO customer = new UserDTO();
            customer.setId(request.getCustomer().getId());
            customer.setEmail(request.getCustomer().getEmail());
            customer.setFullName(request.getCustomer().getFullName());
            customer.setRole(request.getCustomer().getRole());
            dto.setCustomer(customer);
        }

        if (request.getUploadedFiles() != null) {
            List<UploadedFileDto> files = request.getUploadedFiles().stream()
                    .map(f -> {
                        UploadedFileDto ufd = new UploadedFileDto();
                        ufd.setId(f.getId());
                        ufd.setFilePath(f.getFilePath());
                        ufd.setUploadedAt(f.getUploadedAt());
                        return ufd;
                    })
                    .collect(Collectors.toList());
            dto.setUploadedFiles(files);
        }

        if (request.getReport() != null) {
            dto.setTechnicianDescription(request.getReport().getRepairSummary());
        }

        return dto;
    }
}
