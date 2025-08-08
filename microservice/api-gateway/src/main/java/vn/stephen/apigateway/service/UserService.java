package vn.stephen.apigateway.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import vn.stephen.apigateway.constants.ErrorCode;
import vn.stephen.apigateway.dto.User;
import vn.stephen.apigateway.dto.UserDetailsCustom;
import vn.stephen.apigateway.exception.GlobalException;
import vn.stephen.apigateway.rest.AuthServiceRest;

import java.util.Locale;
import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final MessageSource messageSource;
    private final AuthServiceRest authServiceRest;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = authServiceRest.getUserByEmail(username);
        return user.map(UserDetailsCustom::new)
                .orElseThrow(() -> new GlobalException(
                        ErrorCode.NOT_FOUND,
                        messageSource.getMessage("user.not.exist", new Object[]{username},
                                Locale.getDefault())));
    }
}
