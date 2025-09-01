package com.repair_service.repairsystem.dto.repair;

import java.util.List;

public class RepairRequestCreateDto {
    private String description;
    private String deviceModelName;
    private String manufacturer;
    private String category;
    private List<String> imagePaths; // opcjonalnie, jeśli planujesz używać

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getDeviceModelName() { return deviceModelName; }
    public void setDeviceModelName(String deviceModelName) { this.deviceModelName = deviceModelName; }

    public String getManufacturer() { return manufacturer; }
    public void setManufacturer(String manufacturer) { this.manufacturer = manufacturer; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public List<String> getImagePaths() { return imagePaths; }
    public void setImagePaths(List<String> imagePaths) { this.imagePaths = imagePaths; }
}
