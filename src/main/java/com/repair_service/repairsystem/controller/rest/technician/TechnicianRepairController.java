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
import com.repair_service.repairsystem.security.UserDetailsImpl;
import com.repair_service.repairsystem.service.PDFService;
import com.repair_service.repairsystem.service.RepairRequestService;
import com.repair_service.repairsystem.service.EmailService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Kontroler technika
 */
@RestController
@RequestMapping("/api/technician/repairs")
public class TechnicianRepairController {

    private final RepairRequestService repairRequestService;
    private final RepairRequestRepository repairRequestRepository;
    private final RepairReportRepository repairReportRepository;
    private final UserRepository userRepository;
    private final PDFService pdfService;
    private final EmailService emailService;

    public TechnicianRepairController(RepairRequestService repairRequestService,
                                      RepairRequestRepository repairRequestRepository,
                                      RepairReportRepository repairReportRepository,
                                      UserRepository userRepository,
                                      PDFService pdfService,
                                      EmailService emailService) {
        this.repairRequestService = repairRequestService;
        this.repairRequestRepository = repairRequestRepository;
        this.repairReportRepository = repairReportRepository;
        this.userRepository = userRepository;
        this.pdfService = pdfService;
        this.emailService = emailService;
    }

    // Pobranie wszystkich zgłoszeń technika
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

        // status w repair_request
        if (payload.getStatus() != null && !payload.getStatus().isBlank()) {
            rr.setStatus(payload.getStatus());
        }
        repairRequestRepository.save(rr);

        // opis technika - repair_report.repair_summary
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

    //  ENDPOINT PDF
    @GetMapping("/{id}/report-pdf")
    public ResponseEntity<InputStreamResource> downloadRepairPdf(@PathVariable Long id) {
        RepairReport report = repairReportRepository.findByRepairRequestId(id)
                .orElseThrow(() -> new RuntimeException("Brak raportu dla zgłoszenia " + id));

        var bis = pdfService.generateRepairReportPdf(report.getId());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=repair-report-" + id + ".pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }

    //  ENDPOINT wysyłki maila z PDF
    @PostMapping("/{id}/send-email")
    public ResponseEntity<?> sendRepairEmail(@PathVariable Long id) {
        // Pobranie zgłoszenia
        RepairRequest repair = repairRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono zgłoszenia"));

        if (repair.getCustomer() == null || repair.getCustomer().getEmail() == null) {
            return ResponseEntity.badRequest().body("Brak e-maila klienta");
        }

        String email = repair.getCustomer().getEmail();
        String subject = "Twoja naprawa została zakończona!";
        String body = "Zgłoszenie ID: " + repair.getId() + " zostało zakończone.\n" +
                "Możesz pobrać raport PDF.";

        // Generowanie PDF i wysyłka maila
        RepairReport report = repair.getReport();
        if (report != null) {
            var pdfStream = pdfService.generateRepairReportPdf(report.getId());
            emailService.sendEmailWithAttachment(email, subject, body, pdfStream, "repair-report-" + repair.getId() + ".pdf");
        } else {
            emailService.sendEmail(email, subject, body);
        }

        return ResponseEntity.ok("Email wysłany");
    }
}
