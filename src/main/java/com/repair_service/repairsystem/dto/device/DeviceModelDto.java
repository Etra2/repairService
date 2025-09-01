package com.repair_service.repairsystem.dto.device;

import java.util.List;

public class DeviceModelDto {
    private Long id;
    private String manufacturer;
    private String modelName;
    private String category;
    private List<Long> repairRequestIds;

    public DeviceModelDto() {}

    public DeviceModelDto(Long id, String manufacturer, String modelName, String category, List<Long> repairRequestIds) {
        this.id = id;
        this.manufacturer = manufacturer;
        this.modelName = modelName;
        this.category = category;
        this.repairRequestIds = repairRequestIds;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getManufacturer() { return manufacturer; }
    public void setManufacturer(String manufacturer) { this.manufacturer = manufacturer; }

    public String getModelName() { return modelName; }
    public void setModelName(String modelName) { this.modelName = modelName; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public List<Long> getRepairRequestIds() { return repairRequestIds; }
    public void setRepairRequestIds(List<Long> repairRequestIds) { this.repairRequestIds = repairRequestIds; }
}
