package com.repair_service.repairsystem.controller.rest.mapper;

import com.repair_service.repairsystem.dto.device.DeviceModelDto;
import com.repair_service.repairsystem.dto.repair.RepairRequestDto;
import com.repair_service.repairsystem.entity.RepairRequest;
import com.repair_service.repairsystem.entity.UploadedFile;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Klasa pomocnicza (statyczny mapper), która zamienia encję RepairRequest na DTO.
 * Dzięki temu unikamy powielania kodu w kontrolerach i serwisach.
 */
public class RepairMapper {

    /**
     * Mapuje obiekt RepairRequest (encja) na RepairRequestDto.
     * @param request encja RepairRequest
     * @return DTO RepairRequestDto
     */
    public static RepairRequestDto mapToDto(RepairRequest request) {
        RepairRequestDto dto = new RepairRequestDto();

        // Podstawowe pola naprawy
        dto.setId(request.getId());
        dto.setTrackingId(request.getTrackingId());
        dto.setDeviceSerialNumber(request.getDeviceSerialNumber());
        dto.setDescription(request.getDescription());
        dto.setImagePath1(request.getImagePath1());
        dto.setImagePath2(request.getImagePath2());
        dto.setImagePath3(request.getImagePath3());
        dto.setStatus(request.getStatus());
        dto.setCreatedAt(request.getCreatedAt());

        // ID klienta
        if (request.getCustomer() != null) {
            dto.setCustomerId(request.getCustomer().getId());
        }

        // Mapowanie DeviceModel na DTO (zamiast modelId)
        if (request.getModel() != null) {
            DeviceModelDto modelDto = new DeviceModelDto(
                    request.getModel().getId(),
                    request.getModel().getManufacturer(),
                    request.getModel().getModelName(),
                    request.getModel().getCategory(),
                    null // nie przekazujemy listy requestów w DTO
            );
            dto.setDeviceModel(modelDto);
        }

        // ID raportu, jeśli istnieje
        dto.setReportId(request.getReport() != null ? request.getReport().getId() : null);

        // Mapowanie listy załączników (UploadedFile) na listę ich ID
        List<Long> uploadedFileIds = request.getUploadedFiles() != null
                ? request.getUploadedFiles().stream()
                .map(UploadedFile::getId)
                .collect(Collectors.toList())
                : null;
        dto.setUploadedFileIds(uploadedFileIds);

        return dto;
    }
}
