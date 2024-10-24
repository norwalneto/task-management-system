package com.meusprojetos.taskmanagementsystem.model;

public class AuthRequest {

    private String username;
    private String password;

    // Construtor
    public AuthRequest() {
    }

    // Construtor com parametros
    public AuthRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Getter e Setters

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
