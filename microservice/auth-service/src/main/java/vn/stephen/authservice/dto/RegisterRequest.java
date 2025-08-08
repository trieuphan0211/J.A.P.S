package vn.stephen.authservice.dto;

import lombok.Data;
import lombok.Getter;

@Data
public class RegisterRequest {
   private String email;
    private String firstName;
    private String lastName;
    private String gender;
    private String phone;
    private String password;
    private String role;
    private Boolean isVerified;
}

