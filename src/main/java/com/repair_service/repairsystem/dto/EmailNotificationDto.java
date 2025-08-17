package com.repair_service.repairsystem.dto;

// dto do wysyłania powiadomień email
public class EmailNotificationDto {
    private String to; // adres odbiorcy
    private String subject; // temat wiadomości
    private String body; // treść wiadomości

    public EmailNotificationDto(String to, String subject, String body) {
        this.to = to;
        this.subject = subject;
        this.body = body;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
