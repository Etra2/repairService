package com.repair_service.repairsystem.service.impl;

import com.repair_service.repairsystem.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage; // Spring Boot 3 + Jakarta Mail
import java.io.ByteArrayInputStream;

/**
 * Implementacja EmailService używająca JavaMailSender z możliwością wysyłki PDF jako załącznik.
 */
@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Autowired
    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * Wysyła prostego maila (bez załączników)
     *
     * @param to      adres odbiorcy
     * @param subject temat maila
     * @param text    treść maila
     */
    @Override
    public void sendEmail(String to, String subject, String text) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8"); // false = bez multipart
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text);

            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Błąd przy wysyłaniu maila", e);
        }
    }

    /**
     * Wysyła maila z PDF jako załącznik
     *
     * @param to        adres odbiorcy
     * @param subject   temat maila
     * @param text      treść maila
     * @param pdfStream strumień PDF do załączenia
     * @param filename  nazwa pliku PDF w mailu
     */
    public void sendEmailWithAttachment(String to, String subject, String text, ByteArrayInputStream pdfStream, String filename) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8"); // true = multipart

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text);

            // Dodanie PDF jako załącznik
            helper.addAttachment(filename, new ByteArrayResource(pdfStream.readAllBytes()));

            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Błąd przy wysyłaniu maila z załącznikiem", e);
        }
    }
}
