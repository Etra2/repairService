package com.repair_service.repairsystem.dto.graphq;

import java.util.List;

public class RepairRequestInput {
    private String description;
    private String deviceType;
    private List<String> photos;

    public RepairRequestInput() {}
    public RepairRequestInput(String description, String deviceType, List<String> photos) {
        this.description = description;
        this.deviceType = deviceType;
        this.photos = photos;
    }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getDeviceType() { return deviceType; }
    public void setDeviceType(String deviceType) { this.deviceType = deviceType; }

    public List<String> getPhotos() { return photos; }
    public void setPhotos(List<String> photos) { this.photos = photos; }
}
