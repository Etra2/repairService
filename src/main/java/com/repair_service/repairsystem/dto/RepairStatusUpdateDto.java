package com.repair_service.repairsystem.dto;

// dto do aktualizacji statusu zgłoszenia naprawy
public class RepairStatusUpdateDto {
    private String status; // nowy status (np. "IN_PROGRESS", "DONE", "REJECTED")

    public RepairStatusUpdateDto(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
