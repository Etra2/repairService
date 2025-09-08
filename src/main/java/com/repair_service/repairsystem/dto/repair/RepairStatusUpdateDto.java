package com.repair_service.repairsystem.dto.repair;

// dto do aktualizacji statusu zgłoszenia naprawy z modala technika
public class RepairStatusUpdateDto {

    private String status; // nowy status (np. "IN_PROGRESS", "DONE", "REJECTED")
    private String technicianDescription; // opis technika wpisany w modal

    public RepairStatusUpdateDto() {}

    public RepairStatusUpdateDto(String status, String technicianDescription) {
        this.status = status;
        this.technicianDescription = technicianDescription;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTechnicianDescription() {
        return technicianDescription;
    }

    public void setTechnicianDescription(String technicianDescription) {
        this.technicianDescription = technicianDescription;
    }
}
