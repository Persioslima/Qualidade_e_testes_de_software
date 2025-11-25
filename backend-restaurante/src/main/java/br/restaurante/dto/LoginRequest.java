package br.restaurante.dto;

public class LoginRequest {
    private String email;
    private String senha;

    // Construtor padrão (opcional, mas boa prática)
    public LoginRequest() {
    }

    // Construtor com todos os campos (opcional)
    public LoginRequest(String email, String senha) {
        this.email = email;
        this.senha = senha;
    }

    // Getters
    public String getEmail() {
        return email;
    }

    public String getSenha() {
        return senha;
    }

    // Setters
    public void setEmail(String email) {
        this.email = email;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}