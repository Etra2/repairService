package com.repair_service.repairsystem.dto;

import java.util.List;

// dto użytkownika - encja user - zwraca dane użytkownika bez hasła
public class UserDTO {
    private int id;
    private String fullName;
    private String email;
    private String role;
    private List<Long> repairRequestIds;
    private List<Long> reportIds;



    public UserDTO(int id, String fullName, String email, String role, List<Long> repairRequestIds, List<Long> reportIds) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.role = role;
        this.repairRequestIds = repairRequestIds;
        this.reportIds = reportIds;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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
