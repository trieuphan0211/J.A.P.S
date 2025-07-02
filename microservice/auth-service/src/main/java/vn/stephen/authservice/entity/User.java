package vn.stephen.authservice.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;
import java.util.UUID;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(unique = true)
    private String email;
    private String password;
    private String role;
    private boolean is_verified;
    private Timestamp created_at;
    private Timestamp updated_at;
}
