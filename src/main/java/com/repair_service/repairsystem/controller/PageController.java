package com.repair_service.repairsystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    // Strona startowa (np. logowanie / powitanie)
    @GetMapping({"/", "/index"})
    public String index() {
        return "index"; // plik: src/main/resources/templates/index.html
    }

    // Dashboard po zalogowaniu
    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard"; // plik: src/main/resources/templates/dashboard.html
    }

    // Formularz dodania zgłoszenia
    @GetMapping("/repair-form")
    public String repairForm() {
        return "repair_form"; // plik: src/main/resources/templates/repair_form.html
    }

    // Strona do sprawdzania statusu naprawy
    @GetMapping("/repair-status")
    public String repairStatus() {
        return "repair_status"; // plik: src/main/resources/templates/repair_status.html
    }
}
