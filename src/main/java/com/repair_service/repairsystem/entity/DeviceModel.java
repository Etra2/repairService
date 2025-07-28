package com.repair_service.repairsystem.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "device_model", uniqueConstraints = @UniqueConstraint(columnNames = {"manufacturer", "modelName"}))
public class DeviceModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String manufacturer;
    private String modelName;
    private String category;

    @OneToMany(mappedBy = "model")
    private List<RepairRequest> repairRequests;

    public DeviceModel() {
    }

    public DeviceModel(Long id, String manufacturer, String modelName, String category, List<RepairRequest> repairRequests) {
        this.id = id;
        this.manufacturer = manufacturer;
        this.modelName = modelName;
        this.category = category;
        this.repairRequests = repairRequests;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<RepairRequest> getRepairRequests() {
        return repairRequests;
    }

    public void setRepairRequests(List<RepairRequest> repairRequests) {
        this.repairRequests = repairRequests;
    }
}
