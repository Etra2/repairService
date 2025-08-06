package com.repair_service.repairsystem.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "repair_report")
public class RepairReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // podsumowanie naprawy (np. co zostało zrobione)
    @Column(columnDefinition = "TEXT", nullable = false)
    private String repairSummary;

    // uwagi technika
    @Column(columnDefinition = "TEXT")
    private String technicianNotes;

    // koszt naprawy
    private BigDecimal cost;

    // data zakończenia naprawy
    private LocalDateTime repairedAt = LocalDateTime.now();


    @OneToOne
    @JoinColumn(name = "repair_id", unique = true)
    private RepairRequest repairRequest;

    // technik, który wykonał naprawę
    @ManyToOne
    @JoinColumn(name = "technician_id")
    private User technician;

    // konstruktor domyślny
    public RepairReport() {
    }

    // konstruktor pełny
    public RepairReport(Long id, String repairSummary, String technicianNotes, BigDecimal cost,
                        LocalDateTime repairedAt, RepairRequest repairRequest, User technician) {
        this.id = id;
        this.repairSummary = repairSummary;
        this.technicianNotes = technicianNotes;
        this.cost = cost;
        this.repairedAt = repairedAt;
        this.repairRequest = repairRequest;
        this.technician = technician;
    }

    // gettery i settery

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
