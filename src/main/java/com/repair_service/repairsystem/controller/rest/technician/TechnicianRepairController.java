package com.repair_service.repairsystem.controller.rest.technician;

import com.repair_service.repairsystem.controller.rest.mapper.RepairMapper;
import com.repair_service.repairsystem.dto.repair.RepairRequestDto;
import com.repair_service.repairsystem.entity.RepairRequest;
import com.repair_service.repairsystem.service.RepairRequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Kontroler REST dla techników (ROLE_TECHNICIAN).
 * Tutaj technik może:
 *  - pobrać wszystkie zgłoszenia klientów
 *  - zaktualizować status naprawy
 */
@RestController
@RequestMapping("/api/technician/repairs")
public class TechnicianRepairController {

    private final RepairRequestService repairRequestService;

    public TechnicianRepairController(RepairRequestService repairRequestService) {
        this.repairRequestService = repairRequestService;
    }

    // Technik pobiera wszystkie zgłoszenia
    @GetMapping("/all")
    public ResponseEntity<List<RepairRequestDto>> getAllRepairs() {
        List<RepairRequest> repairs = repairRequestService.getAllRepairs();
        List<RepairRequestDto> dtos = repairs.stream()
                .map(RepairMapper::mapToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    // Technik zmienia status zgłoszenia
    @PostMapping("/{id}/update-status")
    public ResponseEntity<RepairRequestDto> updateRepairStatus(
            @PathVariable Long id,
            @RequestParam String newStatus) {
        RepairRequest updated = repairRequestService.updateRepairStatus(id, newStatus);
        return ResponseEntity.ok(RepairMapper.mapToDto(updated));
    }
}
