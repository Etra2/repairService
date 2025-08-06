package com.repair_service.repairsystem.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "repair_request")
public class RepairRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String trackingId;

    private String deviceSerialNumber;

    @Column (columnDefinition = "TEXT")
    private String description;

    // ścieżki do zdjęć
    private String imagePath1;
    private String imagePath2;
    private String imagePath3;

    private String status;

    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private User customer;

    @ManyToOne
    @JoinColumn(name = "model_id")
    private DeviceModel model;

    @OneToOne(mappedBy = "repairRequest", cascade = CascadeType.ALL)
    private RepairReport report;

    @OneToMany(mappedBy = "repairRequest", cascade = CascadeType.ALL)
    private List<UploadedFile> uploadedFiles;

    public RepairRequest() {
    }

    public RepairRequest(Long id, String trackingId, String deviceSerialNumber, String description, String imagePath1, String imagePath2, String imagePath3, String status, LocalDateTime createdAt, User customer, DeviceModel model, RepairReport report, List<UploadedFile> uploadedFiles) {
        this.id = id;
        this.trackingId = trackingId;
        this.deviceSerialNumber = deviceSerialNumber;
        this.description = description;
        this.imagePath1 = imagePath1;
        this.imagePath2 = imagePath2;
        this.imagePath3 = imagePath3;
        this.status = status;
        this.createdAt = createdAt;
        this.customer = customer;
        this.model = model;
        this.report = report;
        this.uploadedFiles = uploadedFiles;
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

    public User getCustomer() {
        return customer;
    }

    public void setCustomer(User customer) {
        this.customer = customer;
    }

    public DeviceModel getModel() {
        return model;
    }

    public void setModel(DeviceModel model) {
        this.model = model;
    }

    public RepairReport getReport() {
        return report;
    }

    public void setReport(RepairReport report) {
        this.report = report;
    }

    public List<UploadedFile> getUploadedFiles() {
        return uploadedFiles;
    }

    public void setUploadedFiles(List<UploadedFile> uploadedFiles) {
        this.uploadedFiles = uploadedFiles;
    }
}
