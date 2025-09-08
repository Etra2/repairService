package com.repair_service.repairsystem.dto.repair;

import com.repair_service.repairsystem.dto.auth.UserDTO;
import com.repair_service.repairsystem.dto.device.DeviceModelDto;

import java.time.LocalDateTime;
import java.util.List;

public class RepairRequestDto {

    private Long id;
    private String trackingId;
    private String deviceSerialNumber;
    private String description;
    private String imagePath1;
    private String imagePath2;
    private String imagePath3;
    private String status;
    private LocalDateTime createdAt;
    private Long customerId;
    private DeviceModelDto deviceModel;
    private Long reportId;
    private List<Long> uploadedFileIds;

    // nowe pola dla modala
    private UserDTO customer;
    private List<UploadedFileDto> uploadedFiles;
    private String technicianDescription;

    // gettery i settery dla wszystkich pól
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getTrackingId() {
        return trackingId;
    }
    public void setTrackingId(String trackingId) {
        this.trackingId = trackingId;
    }
    public String getDeviceSerialNumber() {
        return deviceSerialNumber;
    }
    public void setDeviceSerialNumber(String deviceSerialNumber) {
        this.deviceSerialNumber = deviceSerialNumber;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getImagePath1() {
        return imagePath1;
    }
    public void setImagePath1(String imagePath1) {
        this.imagePath1 = imagePath1;
    }
    public String getImagePath2() {
        return imagePath2;
    }
    public void setImagePath2(String imagePath2) {
        this.imagePath2 = imagePath2;
    }
    public String getImagePath3() {
        return imagePath3;
    }
    public void setImagePath3(String imagePath3) {
        this.imagePath3 = imagePath3;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    public Long getCustomerId() {
        return customerId;
    }
    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }
    public DeviceModelDto getDeviceModel() {
        return deviceModel;
    }
    public void setDeviceModel(DeviceModelDto deviceModel) {
        this.deviceModel = deviceModel;
    }
    public Long getReportId() {
        return reportId;
    }
    public void setReportId(Long reportId) {
        this.reportId = reportId;
    }
    public List<Long> getUploadedFileIds() {
        return uploadedFileIds;
    }
    public void setUploadedFileIds(List<Long> uploadedFileIds) {
        this.uploadedFileIds = uploadedFileIds;
    }
    public UserDTO getCustomer() {
        return customer;
    }
    public void setCustomer(UserDTO customer) {
        this.customer = customer;
    }
    public List<UploadedFileDto> getUploadedFiles() {
        return uploadedFiles;
    }
    public void setUploadedFiles(List<UploadedFileDto> uploadedFiles) {
        this.uploadedFiles = uploadedFiles;
    }
    public String getTechnicianDescription() {
        return technicianDescription;
    }
    public void setTechnicianDescription(String technicianDescription) {
        this.technicianDescription = technicianDescription;
    }
}
