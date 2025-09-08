package com.repair_service.repairsystem.dto.auth;

import java.util.List;

public class UserDTO {

    private Long id; // zmienione z int na Long
    private String fullName;
    private String email;
    private String role;
    private List<Long> repairRequestIds;
    private List<Long> reportIds;

    // konstruktor domyślny
    public UserDTO() {}

    public UserDTO(Long id, String fullName, String email, String role,
                   List<Long> repairRequestIds, List<Long> reportIds) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.role = role;
        this.repairRequestIds = repairRequestIds;
        this.reportIds = reportIds;
    }

    // gettery i settery
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getFullName() {
        return fullName;
    }
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }
    public List<Long> getRepairRequestIds() {
        return repairRequestIds;
    }
    public void setRepairRequestIds(List<Long> repairRequestIds) {
        this.repairRequestIds = repairRequestIds;
    }
    public List<Long> getReportIds() {
        return reportIds;
    }
    public void setReportIds(List<Long> reportIds) {
        this.reportIds = reportIds;
    }
}
