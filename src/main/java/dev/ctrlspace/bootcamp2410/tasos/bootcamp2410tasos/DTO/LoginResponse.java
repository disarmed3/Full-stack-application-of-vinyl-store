package dev.ctrlspace.bootcamp2410.tasos.bootcamp2410tasos.DTO;

public class LoginResponse {
    private String email;
    private String role;

    public LoginResponse(String email, String role) {
        this.email = email;
        this.role = role;
    }

    // Getters and Setters
    public String getEmail() {
        return email;
    }

   public String getRole() {
        return role;
    }

}

