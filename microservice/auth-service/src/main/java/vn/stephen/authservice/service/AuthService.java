package vn.stephen.authservice.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import vn.stephen.authservice.constants.ErrorCode;
import vn.stephen.authservice.dto.AuthResponse;
import vn.stephen.authservice.dto.RegisterRequest;
import vn.stephen.authservice.exception.GlobalException;
import vn.stephen.authservice.repository.UserRepository;
import vn.stephen.authservice.util.JwtUtil;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

@Log4j2
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final MessageSource messageSource;

    public AuthService(UserRepository userRepository, JwtUtil jwtUtil, MessageSource messageSource) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.messageSource = messageSource;
    }

    public Map<String, UUID> register(RegisterRequest request, Locale locale) {
        Map<String, UUID> response = new HashMap<>();
        Object responseDB = userRepository.registerUser(request.getEmail(), jwtUtil.hashPassword(request.getPassword()),
                request.getRole(), request.getIs_verified());
        if (responseDB instanceof Object[] resultArray) {
            Integer statusCode = (Integer) resultArray[0]; // Assuming the first column is status code
            UUID userId = (UUID) resultArray[1]; // Assuming the second column is user id
            if (statusCode == 1) {
                response.put("userId", userId);
            }else {
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
                                messageSource.getMessage("user.invalid.type", new Object[]{request.getIs_verified()}, locale)
                        );
                    default:
                        throw new GlobalException(
                                ErrorCode.UNKNOWN,
                                messageSource.getMessage("user.unknown", null, locale)
                        );
                }
            }
        }
        return null;
    }
}
