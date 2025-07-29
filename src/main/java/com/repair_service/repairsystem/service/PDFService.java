package com.repair_service.repairsystem.service;

import java.io.ByteArrayInputStream;

public interface PDFService {
    ByteArrayInputStream generateRepairReportPdf(Long reportId);
}
