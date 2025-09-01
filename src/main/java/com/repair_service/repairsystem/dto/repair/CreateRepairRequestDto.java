package com.repair_service.repairsystem.dto.repair;

public class CreateRepairRequestDto {
    private String description;
    private String deviceModelName;
    private String manufacturer;
    private String category;
    private String deviceSerialNumber;

    public CreateRepairRequestDto() {}

    public CreateRepairRequestDto(String description, String deviceModelName,
                                  String manufacturer, String category,
                                  String deviceSerialNumber) {
        this.description = description;
        this.deviceModelName = deviceModelName;
        this.manufacturer = manufacturer;
        this.category = category;
        this.deviceSerialNumber = deviceSerialNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDeviceModelName() {
        return deviceModelName;
    }

    public void setDeviceModelName(String deviceModelName) {
        this.deviceModelName = deviceModelName;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDeviceSerialNumber() {
        return deviceSerialNumber;
    }

    public void setDeviceSerialNumber(String deviceSerialNumber) {
        this.deviceSerialNumber = deviceSerialNumber;
    }
}
