package com.repair_service.repairsystem.dto.auth;

// dto do rejestracji użytkownika - przesyłanie z frontu do rest api
public class AuthRequestDto {
    private String fullName;
    private String email; // email - login
    private String password; // hasło - szyfrowane w serwisie

    public AuthRequestDto() {}

    public AuthRequestDto(String fullName, String email, String password) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
