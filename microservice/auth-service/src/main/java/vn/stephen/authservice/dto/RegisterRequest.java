package vn.stephen.authservice.dto;

import lombok.Data;
import lombok.Getter;

@Data
public class RegisterRequest {
   private String email;
    private String password;
    private String role;
    private Boolean is_verified;
}

