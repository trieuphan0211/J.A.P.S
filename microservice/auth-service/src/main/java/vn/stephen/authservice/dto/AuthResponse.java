package vn.stephen.authservice.dto;

public record AuthResponse(String accessToken, String refreshToken,Integer expiresIn,Integer refreshExpiresIn,String tokenType) {}

