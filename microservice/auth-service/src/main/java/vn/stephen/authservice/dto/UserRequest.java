package vn.stephen.authservice.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserRequest {
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private String gender;
    private String phone;
}
