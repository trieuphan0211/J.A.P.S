package vn.stephen.authservice.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {
    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    public String hashPassword(String plainPassword) {
        return encoder.encode(plainPassword);
    }
    public boolean verifyPassword(String plainPassword, String hashedPassword) {
        return encoder.matches(plainPassword, hashedPassword);
    }
}
