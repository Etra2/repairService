package com.repair_service.repairsystem.service;

import java.io.ByteArrayInputStream;

public interface EmailService {

    void sendEmail(String to, String subject, String text);

    // metoda do wysyłki maila z PDF
    void sendEmailWithAttachment(String to, String subject, String text, ByteArrayInputStream pdfStream, String filename);
}



