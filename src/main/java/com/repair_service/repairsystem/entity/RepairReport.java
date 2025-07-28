package com.repair_service.repairsystem.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "repair_report")
public class RepairReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String repairSummary;

    private LocalDateTime repairedAt = LocalDateTime.now();

    @OneToOne
    @JoinColumn(name = "repair_id", unique = true)
    private RepairRequest repairRequest;

    @ManyToOne
    @JoinColumn(name = "technician_id")
    private User technician;

    public RepairReport() {
    }

    public RepairReport(Long id, String repairSummary, LocalDateTime repairedAt, RepairRequest repairRequest, User technician) {
        this.id = id;
        this.repairSummary = repairSummary;
        this.repairedAt = repairedAt;
        this.repairRequest = repairRequest;
        this.technician = technician;
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

    public LocalDateTime getRepairedAt() {
        return repairedAt;
    }

    public void setRepairedAt(LocalDateTime repairedAt) {
        this.repairedAt = repairedAt;
    }

    public RepairRequest getRepairRequest() {
        return repairRequest;
    }

    public void setRepairRequest(RepairRequest repairRequest) {
        this.repairRequest = repairRequest;
    }

    public User getTechnician() {
        return technician;
    }

    public void setTechnician(User technician) {
        this.technician = technician;
    }
}
