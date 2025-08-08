package vn.stephen.apigateway.dto;

import java.sql.Timestamp;
import java.util.UUID;

import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class User {
    private UUID id;
    private String email;
    private String password;
    private String role;
    private boolean is_verified;
    private Timestamp created_at;
    private Timestamp updated_at;
}
