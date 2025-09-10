package com.repair_service.repairsystem.service.impl;

import com.repair_service.repairsystem.entity.RepairReport;
import com.repair_service.repairsystem.repository.RepairReportRepository;
import com.repair_service.repairsystem.service.PDFService;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/* Serwis generujący raport PDF dla zgłoszeń napraw.
   Obsługuje polskie znaki dzięki czcionce TTF - Arial
 */
@Service
public class PDFServiceImpl implements PDFService {

    private final RepairReportRepository repairReportRepository;

    public PDFServiceImpl(RepairReportRepository repairReportRepository) {
        this.repairReportRepository = repairReportRepository;
    }

    @Override
    public ByteArrayInputStream generateRepairReportPdf(Long reportId) {
        // Pobranie raportu z bazy
        RepairReport report = repairReportRepository.findById(reportId)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono raportu o ID: " + reportId));

        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            // ===================== CZCIONKI =====================
            // Ścieżka do czcionki TTF w resources/fonts/arial.ttf
            BaseFont baseFont = BaseFont.createFont(
                    "src/main/resources/fonts/arial.ttf", // tutaj podaj faktyczną czcionkę
                    BaseFont.IDENTITY_H,  // umożliwia polskie znaki
                    BaseFont.EMBEDDED     // czcionka osadzona w PDF
            );
            Font headerFont = new Font(baseFont, 16, Font.BOLD);
            Font fieldFont = new Font(baseFont, 12, Font.BOLD);
            Font valueFont = new Font(baseFont, 12, Font.NORMAL);

            // ===================== NAGŁÓWEK =====================
            Paragraph header = new Paragraph("Raport naprawy", headerFont);
            header.setAlignment(Element.ALIGN_CENTER);
            document.add(header);
            document.add(new Paragraph(" ")); // odstęp

            // ===================== DANE ZGŁOSZENIA =====================
            document.add(new Paragraph("Opis naprawy:", fieldFont));
            document.add(new Paragraph(report.getRepairRequest().getDescription(), valueFont));
            document.add(new Paragraph(" "));

            document.add(new Paragraph("Uwagi serwisanta:", fieldFont));
            document.add(new Paragraph(report.getRepairSummary(), valueFont));
            document.add(new Paragraph(" "));

            document.add(new Paragraph("Email klienta:", fieldFont));
            String clientEmail = report.getRepairRequest().getCustomer() != null ?
                    report.getRepairRequest().getCustomer().getEmail() : "brak";
            document.add(new Paragraph(clientEmail, valueFont));
            document.add(new Paragraph(" "));

            document.add(new Paragraph("ID technika:", fieldFont));
            String technicianId = report.getTechnician() != null ?
                    report.getTechnician().getId().toString() : "brak";
            document.add(new Paragraph(technicianId, valueFont));
            document.add(new Paragraph(" "));

            // ===================== PŁATNOŚĆ =====================
            document.add(new Paragraph("Płatność:", fieldFont));
            document.add(new Paragraph("BRAK OPŁAT", valueFont));
            document.add(new Paragraph(" "));

            // ===================== STATUS I DATA ZAKOŃCZENIA =====================
            document.add(new Paragraph("Status:", fieldFont));
            document.add(new Paragraph(report.getRepairRequest().getStatus(), valueFont));
            document.add(new Paragraph("Data zakończenia:", fieldFont));
            document.add(new Paragraph(report.getRepairedAt() != null ?
                    report.getRepairedAt().toString() : "brak", valueFont));

            document.close();
        } catch (DocumentException | java.io.IOException e) {
            throw new RuntimeException("Wystąpił błąd podczas generowania PDF", e);
        }

        // Zwraca PDF w formie strumienia bajtów, gotowy do pobrania przez klienta
        return new ByteArrayInputStream(out.toByteArray());
    }
}
