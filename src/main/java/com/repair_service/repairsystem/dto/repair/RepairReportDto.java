package com.repair_service.repairsystem.dto.repair;

import java.math.BigDecimal;
import java.time.LocalDateTime;

// raport z naprawy (dla technika/klienta)
public class RepairReportDto {
    private Long id;
    private String repairSummary;
    private String technicianNotes;
    private BigDecimal cost;
    private LocalDateTime repairedAt;
    private Long repairRequestId;
    private Long technicianId;


    public RepairReportDto(String repairSummary, Long id, String technicianNotes, BigDecimal cost, LocalDateTime repairedAt, Long repairRequestId, Long technicianId) {
        this.repairSummary = repairSummary;
        this.id = id;
        this.technicianNotes = technicianNotes;
        this.cost = cost;
        this.repairedAt = repairedAt;
        this.repairRequestId = repairRequestId;
        this.technicianId = technicianId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRepairSummary() {
        return repairSummary;
    }

    public void setRepairSummary(String repairSummary) {
        this.repairSummary = repairSummary;
    }

    public String getTechnicianNotes() {
        return technicianNotes;
    }

    public void setTechnicianNotes(String technicianNotes) {
        this.technicianNotes = technicianNotes;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public LocalDateTime getRepairedAt() {
        return repairedAt;
    }

    public void setRepairedAt(LocalDateTime repairedAt) {
        this.repairedAt = repairedAt;
    }

    public Long getRepairRequestId() {
        return repairRequestId;
    }

    public void setRepairRequestId(Long repairRequestId) {
        this.repairRequestId = repairRequestId;
    }

    public Long getTechnicianId() {
        return technicianId;
    }

    public void setTechnicianId(Long technicianId) {
        this.technicianId = technicianId;
    }
}
