package com.repair_service.repairsystem.controller;

import com.repair_service.repairsystem.security.UserDetailsImpl;
import com.repair_service.repairsystem.service.RepairRequestService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@Controller
public class PageController {

    private final RepairRequestService repairRequestService;

    public PageController(RepairRequestService repairRequestService) {
        this.repairRequestService = repairRequestService;
    }

    // Strona startowa / logowanie
    @GetMapping({"/", "/index"})
    public String index() {
        return "index"; // src/main/resources/templates/index.html
    }

    // Dashboard po zalogowaniu
    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard"; // src/main/resources/templates/dashboard.html
    }

    // Formularz dodania zgłoszenia
    @GetMapping("/repair-form")
    public String repairForm() {
        return "repair_form"; // src/main/resources/templates/repair_form.html
    }

    // Strona do sprawdzania statusu naprawy
    @GetMapping("/repair-status")
    public String repairStatus() {
        return "repair_status"; // src/main/resources/templates/repair_status.html
    }

    // 🔹 Technika - lista wszystkich zgłoszeń
    @GetMapping("/technician/repairs")
    public String technicianRepairs(Model model, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        // Wyświetlamy e-mail technika w HTML zamiast fullName
        model.addAttribute("fullName", userDetails.getEmail());
        model.addAttribute("repairs", repairRequestService.getAllRepairs());
        return "technician_repairs"; // src/main/resources/templates/technician_repairs.html
    }
}
