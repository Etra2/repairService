package com.repair_service.repairsystem.controller.rest;

import com.repair_service.repairsystem.entity.RepairRequest;
import com.repair_service.repairsystem.service.RepairRequestService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/repairs")
public class RepairRequestController {

    private final RepairRequestService repairRequestService;

    public RepairRequestController(RepairRequestService repairRequestService) {
        this.repairRequestService = repairRequestService;
    }

    // Endpoint do zakończenia naprawy
    @PutMapping("/complete/{id}")
    public RepairRequest completeRepair(@PathVariable Long id) {
        return repairRequestService.completeRepair(id);
    }
}
