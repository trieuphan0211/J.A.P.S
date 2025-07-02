package vn.stephen.authservice.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import vn.stephen.authservice.dto.AuthResponse;
import vn.stephen.authservice.dto.RegisterRequest;
import vn.stephen.authservice.repository.UserRepository;
import vn.stephen.authservice.util.JwtUtil;

import java.util.Map;
import java.util.UUID;

@Log4j2
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    public Map<String, UUID> register(RegisterRequest request) {
        log.info("Registering user");
        log.info("Register request: {}", request);
        Object responseDB = userRepository.registerUser(request.email(), jwtUtil.hashPassword(request.password()),
                request.role(), request.is_verified());
        log.info("User registered: {}", responseDB);
        return null;
    }
}
