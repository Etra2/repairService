package com.repair_service.repairsystem.controller.rest.technician;

import com.repair_service.repairsystem.controller.rest.mapper.RepairMapper;
import com.repair_service.repairsystem.dto.repair.RepairRequestDto;
import com.repair_service.repairsystem.dto.repair.RepairStatusUpdateDto;
import com.repair_service.repairsystem.entity.RepairReport;
import com.repair_service.repairsystem.entity.RepairRequest;
import com.repair_service.repairsystem.entity.User;
import com.repair_service.repairsystem.repository.RepairReportRepository;
import com.repair_service.repairsystem.repository.RepairRequestRepository;
import com.repair_service.repairsystem.repository.UserRepository;
import com.repair_service.repairsystem.service.RepairRequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import com.repair_service.repairsystem.security.UserDetailsImpl;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/technician/repairs")
public class TechnicianRepairController {

    private final RepairRequestService repairRequestService;
    private final RepairRequestRepository repairRequestRepository;
    private final RepairReportRepository repairReportRepository;
    private final UserRepository userRepository;

    public TechnicianRepairController(RepairRequestService repairRequestService,
                                      RepairRequestRepository repairRequestRepository,
                                      RepairReportRepository repairReportRepository,
                                      UserRepository userRepository) {
        this.repairRequestService = repairRequestService;
        this.repairRequestRepository = repairRequestRepository;
        this.repairReportRepository = repairReportRepository;
        this.userRepository = userRepository;
    }

    // Pobranie wszystkich zgłoszeń technika (Twój istniejący kod)
    @GetMapping("/all")
    public ResponseEntity<List<RepairRequestDto>> getAllRepairs() {
        List<RepairRequest> repairs = repairRequestService.getAllRepairs();
        List<RepairRequestDto> dtos = repairs.stream()
                .map(RepairMapper::mapToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    // GET szczegóły jednego zgłoszenia (modal)
    @GetMapping("/{id}")
    public ResponseEntity<RepairRequestDto> getRepair(@PathVariable Long id) {
        Optional<RepairRequest> opt = repairRequestRepository.findById(id);
        if (opt.isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(RepairMapper.mapToDto(opt.get()));
    }

    // PUT zapis statusu + opisu technika z modala
    @PutMapping("/{id}")
    public ResponseEntity<?> updateRepair(@PathVariable Long id,
                                          @RequestBody RepairStatusUpdateDto payload,
                                          @AuthenticationPrincipal UserDetailsImpl currentUser) {
        Optional<RepairRequest> opt = repairRequestRepository.findById(id);
        if (opt.isEmpty()) return ResponseEntity.notFound().build();

        RepairRequest rr = opt.get();

        // 1) status w repair_request
        if (payload.getStatus() != null && !payload.getStatus().isBlank()) {
            rr.setStatus(payload.getStatus());
        }
        repairRequestRepository.save(rr);

        // 2) opis technika -> repair_report.repair_summary
        if (payload.getTechnicianDescription() != null) {
            RepairReport report = rr.getReport();
            if (report == null) {
                report = new RepairReport();
                report.setRepairRequest(rr);

                // ustawienie technika
                if (currentUser != null) {
                    User tech = userRepository.findById(currentUser.getId()).orElse(null);
                    report.setTechnician(tech);
                }
            }
            report.setRepairSummary(payload.getTechnicianDescription());
            repairReportRepository.save(report);
        }

        return ResponseEntity.ok().build();
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
