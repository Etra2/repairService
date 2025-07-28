package com.repair_service.repairsystem.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(nullable = false)
    private String role; // ROLE_CLIENT, ROLE_TECHNICIAN

    @OneToMany(mappedBy = "customer")
    private List<RepairRequest> repairRequests;

    @OneToMany(mappedBy = "technician")
    private List<RepairReport> reports;

    public User() {
    }

    public User(Long id, String email, String password, String fullName, String role, List<RepairReport> reports, List<RepairRequest> repairRequests) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.role = role;
        this.reports = reports;
        this.repairRequests = repairRequests;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public List<RepairRequest> getRepairRequests() {
        return repairRequests;
    }

    public void setRepairRequests(List<RepairRequest> repairRequests) {
        this.repairRequests = repairRequests;
    }

    public List<RepairReport> getReports() {
        return reports;
    }

    public void setReports(List<RepairReport> reports) {
        this.reports = reports;
    }
}
