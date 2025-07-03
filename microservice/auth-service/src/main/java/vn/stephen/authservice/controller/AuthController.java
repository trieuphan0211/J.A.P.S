package vn.stephen.authservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.stephen.authservice.dto.RegisterRequest;
import vn.stephen.authservice.service.AuthService;

import java.util.Locale;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, UUID>> addUser(@RequestBody RegisterRequest data, @RequestHeader(name = "Accept-Language", required = false) Locale locale) {
        return ResponseEntity.ok(authService.register(data,locale));
    }
}
