package vn.stephen.authservice.dto;

public record RegisterRequest(String email, String password, String role,Boolean is_verified) {}

