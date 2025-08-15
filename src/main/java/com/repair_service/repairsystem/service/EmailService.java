package com.repair_service.repairsystem.service;

public interface EmailService {

    /**
     * Wysyła e-mail do klienta
     * @param to - adres odbiorcy
     * @param subject - temat wiadomości
     * @param text - treść wiadomości
     */
    void sendEmail(String to, String subject, String text);
}

