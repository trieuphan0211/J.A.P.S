package vn.stephen.authservice.service;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import vn.stephen.authservice.constants.ErrorCode;
import vn.stephen.authservice.dto.AuthResponse;
import vn.stephen.authservice.dto.LoginRequest;
import vn.stephen.authservice.dto.RegisterRequest;
import vn.stephen.authservice.dto.UserRequest;
import vn.stephen.authservice.entity.User;
import vn.stephen.authservice.exception.GlobalException;
import vn.stephen.authservice.repository.UserRepository;
import vn.stephen.authservice.rest.UserServiceRest;
import vn.stephen.authservice.util.JwtUtil;

import java.util.*;

@Log4j2
@Service
@RequiredArgsConstructor
public class AuthService {
    @Value("${app.config.jwt.expires-in}")
    private Integer EXPIRES_IN;

    @Value("${app.config.jwt.refresh-expires-in}")
    private Integer REFRESH_EXPIRES_IN;

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final MessageSource messageSource;
    private final AuthenticationManager authenticationManager;
    private final UserServiceRest userServiceRest;


    public Map<String, UUID> register(RegisterRequest request, Locale locale) {
        Map<String, UUID> response = new HashMap<>();
        try {
            Object responseDB = userRepository.registerUser(request.getEmail(), jwtUtil.hashPassword(request.getPassword()),
                    request.getRole(), request.getIsVerified());
            if (responseDB instanceof Object[] resultArray) {
                Integer statusCode = (Integer) resultArray[0]; // Assuming the first column is status code
                UUID userId = (UUID) resultArray[1]; // Assuming the second column is user id
                if (statusCode == 1) {
                    response.put("userId", userId);
                    UserRequest userRequest = new UserRequest(userId, request.getFirstName(), request.getLastName(), request.getEmail(), request.getGender(), request.getPhone());
                    log.info("Creating user with request: {}", userRequest);
                    userServiceRest.createUser(userRequest);
                } else {
                    switch (statusCode) {
                        case 100101:
                            throw new GlobalException(
                                    ErrorCode.MISSING_PARAMETER,
                                    messageSource.getMessage("user.missing.parameter", new Object[]{"firstName"},
                                            locale));
                        case 100102:
                            throw new GlobalException(
                                    ErrorCode.MISSING_PARAMETER,
                                    messageSource.getMessage("user.missing.parameter", new Object[]{"lastName"}, locale)
                            );
                        case 100103:
                            throw new GlobalException(
                                    ErrorCode.MISSING_PARAMETER,
                                    messageSource.getMessage("user.missing.parameter", new Object[]{"email"}, locale)
                            );
                        case 100104:
                            throw new GlobalException(
                                    ErrorCode.MISSING_PARAMETER,
                                    messageSource.getMessage("user.missing.parameter", new Object[]{"role"}, locale)
                            );
                        case 100105:
                            throw new GlobalException(
                                    ErrorCode.MISSING_PARAMETER,
                                    messageSource.getMessage("user.missing.parameter", new Object[]{"status"}, locale)
                            );
                        case 100106:
                            throw new GlobalException(
                                    ErrorCode.MISSING_PARAMETER,
                                    messageSource.getMessage("user.missing.parameter", new Object[]{"password"}, locale)
                            );
                        case 102103:
                            throw new GlobalException(
                                    ErrorCode.EXISTED_EMAIL,
                                    messageSource.getMessage("user.exist.email", new Object[]{request.getEmail()}, locale)
                            );
                        case 103104:
                            throw new GlobalException(
                                    ErrorCode.EXISTED_EMAIL,
                                    messageSource.getMessage("user.invalid.type", new Object[]{request.getRole()}, locale)
                            );
                        case 103105:
                            throw new GlobalException(
                                    ErrorCode.EXISTED_EMAIL,
                                    messageSource.getMessage("user.invalid.type", new Object[]{request.getIsVerified()}, locale)
                            );
                        default:
                            throw new GlobalException(
                                    ErrorCode.UNKNOWN,
                                    messageSource.getMessage("user.unknown", null, locale)
                            );
                    }
                }
            }
            return response;
        } catch (FeignException e) {
            log.error("Error during registration: {}", e.responseBody().orElseThrow());
            userRepository.deleteUserById(response.get("userId"));
            throw new GlobalException(
                    ErrorCode.UNKNOWN,
                    messageSource.getMessage("unknown.error", null, locale)
            );
        }
    }

    public AuthResponse generateAccessToken(LoginRequest request, Locale locale) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()));
        if (authentication.isAuthenticated()) {
            try {
                Optional<User> user = userRepository.findByEmail(request.email());
                if (user.isEmpty()) {
                    throw new GlobalException(
                            ErrorCode.MISSING_PARAMETER,
                            messageSource.getMessage("user.not.found", null,
                                    locale));
                }
                if (!jwtUtil.verifyPassword(request.password(), user.get().getPassword())) {
                    throw new GlobalException(
                            ErrorCode.MISSING_PARAMETER,
                            messageSource.getMessage("auth.password.invalid", null,
                                    locale));
                }
                String accessToken = jwtUtil.generateToken(
                        user.get().getEmail(),
                        "trieu",
                        "trieu",
                        user.get().getRole(),
                        false);
                String refreshToken = jwtUtil.generateToken(
                        user.get().getEmail(),
                        "trieu",
                        "trieu",
                        user.get().getRole(),
                        true);
                return new AuthResponse(accessToken,
                        refreshToken,
                        EXPIRES_IN * 60,
                        REFRESH_EXPIRES_IN * 60,
                        "Bearer"
                );
            } catch (Exception e) {
                throw new GlobalException(
                        ErrorCode.JOSE_EXCEPTION,
                        e.getLocalizedMessage()
                );
            }
        }

        log.info(authentication.toString());
        return null;
    }
}
