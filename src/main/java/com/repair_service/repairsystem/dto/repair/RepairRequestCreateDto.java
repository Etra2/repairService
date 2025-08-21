package com.repair_service.repairsystem.dto.repair;

import java.util.List;

// dto do tworzenia zgłoszenia naprawy
public class RepairRequestCreateDto {
    private String description;
    private Long deviceModelId;
    private List<String> imagePaths;

    public RepairRequestCreateDto(String description, Long deviceModelId, List<String> imagePaths) {
        this.description = description;
        this.deviceModelId = deviceModelId;
        this.imagePaths = imagePaths;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getDeviceModelId() {
        return deviceModelId;
    }

    public void setDeviceModelId(Long deviceModelId) {
        this.deviceModelId = deviceModelId;
    }

    public List<String> getImagePaths() {
        return imagePaths;
    }

    public void setImagePaths(List<String> imagePaths) {
        this.imagePaths = imagePaths;
    }
}
