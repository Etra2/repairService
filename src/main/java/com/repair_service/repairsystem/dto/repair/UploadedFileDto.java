package com.repair_service.repairsystem.dto.repair;

import java.time.LocalDateTime;

public class UploadedFileDto {

    private Long id;
    private String filePath;
    private LocalDateTime uploadedAt;

    public UploadedFileDto() {}

    public UploadedFileDto(Long id, String filePath, LocalDateTime uploadedAt) {
        this.id = id;
        this.filePath = filePath;
        this.uploadedAt = uploadedAt;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getFilePath() {
        return filePath;
    }
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }
    public void setUploadedAt(LocalDateTime uploadedAt) {
        this.uploadedAt = uploadedAt;
    }
}
