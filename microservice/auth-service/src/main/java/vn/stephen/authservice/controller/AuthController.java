package vn.stephen.authservice.controller;

import org.jose4j.jwt.JwtClaims;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.stephen.authservice.dto.AuthResponse;
import vn.stephen.authservice.dto.LoginRequest;
import vn.stephen.authservice.dto.RegisterRequest;
import vn.stephen.authservice.entity.User;
import vn.stephen.authservice.service.AuthService;
import vn.stephen.authservice.service.UserService;
import vn.stephen.authservice.util.JwtUtil;

import java.util.Locale;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    public AuthController(AuthService authService, UserService userService, JwtUtil jwtUtil) {
        this.authService = authService;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, UUID>> addUser(@RequestBody RegisterRequest data, @RequestHeader(name = "Accept-Language", required = false) Locale locale) {
        return ResponseEntity.ok(authService.register(data,locale));
    }
    @PostMapping("/token")
    public ResponseEntity<AuthResponse> getToken(@RequestBody LoginRequest data, @RequestHeader(name = "Accept-Language", required = false) Locale locale) {
        return ResponseEntity.ok(authService.generateAccessToken(data,locale));
    }
    @GetMapping("/user")
    public ResponseEntity<String> getUser(@RequestHeader(name = "Accept-Language", required = false) Locale locale) {
        return ResponseEntity.ok("OK");
    }
    @GetMapping("/user/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email, @RequestHeader(name = "Accept-Language", required = false) Locale locale) {
        return ResponseEntity.ok(userService.getUserByEmail(email));
    }
    @GetMapping("/verify")
    public JwtClaims verifyToken(@RequestHeader(name="Authorization") String authHeader,@RequestHeader(name = "Accept-Language", required = false) Locale locale) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String  token = authHeader.substring(7);
            return jwtUtil.jwtConsumer(token,false);
        }
        return null;
    }
}
