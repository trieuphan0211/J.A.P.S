package vn.stephen.authservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.MessageSource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import vn.stephen.authservice.constants.ErrorCode;
import vn.stephen.authservice.dto.UserDetailsCustom;
import vn.stephen.authservice.entity.User;
import vn.stephen.authservice.exception.GlobalException;
import vn.stephen.authservice.repository.UserRepository;

import java.util.Locale;
import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final MessageSource messageSource;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByEmail(username);
        return user.map(UserDetailsCustom::new)
                .orElseThrow(() -> new GlobalException(
                        ErrorCode.NOT_FOUND,
                        messageSource.getMessage("user.not.exist", new Object[]{username},
                                Locale.getDefault())));
    }
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new GlobalException(
                        ErrorCode.NOT_FOUND,
                        messageSource.getMessage("user.not.exist", new Object[]{email},
                                Locale.getDefault())));
    }
}
