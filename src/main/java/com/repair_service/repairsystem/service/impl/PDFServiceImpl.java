package com.repair_service.repairsystem.service.impl;

import com.repair_service.repairsystem.entity.RepairReport;
import com.repair_service.repairsystem.repository.RepairReportRepository;
import com.repair_service.repairsystem.service.PDFService;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

@Service
public class PDFServiceImpl implements PDFService {

    private final RepairReportRepository repairReportRepository;

    @Autowired
    public PDFServiceImpl(RepairReportRepository repairReportRepository) {
        this.repairReportRepository = repairReportRepository;
    }

    @Override
    public ByteArrayInputStream generateRepairReportPdf(Long reportId) {
        RepairReport report = repairReportRepository.findById(reportId)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono raportu o ID: " + reportId));

        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            // Nagłówek
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
            Paragraph header = new Paragraph("Raport naprawy", headerFont);
            header.setAlignment(Element.ALIGN_CENTER);
            document.add(header);
            document.add(new Paragraph(" ")); // Odstęp

            // Dane z raportu
            document.add(new Paragraph("Opis naprawy: " + report.getRepairRequest().getDescription()));
            document.add(new Paragraph("Uwagi serwisanta: " + report.getTechnicianNotes()));
            document.add(new Paragraph("Koszt naprawy: " + report.getCost()));
            document.add(new Paragraph("Status: " + report.getRepairRequest().getStatus()));
            document.add(new Paragraph("Data zakończenia: " + report.getRepairedAt()));

            document.close();
        } catch (DocumentException e) {
            throw new RuntimeException("Wystąpił błąd podczas generowania PDF", e);
        }

        return new ByteArrayInputStream(out.toByteArray());
    }
}