package vn.stephen.apigateway.rest;

import org.jose4j.jwt.JwtClaims;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import vn.stephen.apigateway.configuration.FeignConfiguration;
import vn.stephen.apigateway.dto.TokenClaimsResponse;
import vn.stephen.apigateway.dto.User;

import java.util.Optional;

@FeignClient(name = "AUTH-SERVICE",configuration = FeignConfiguration.class)
//@RequestMapping("/api/v1")
public interface  AuthServiceRest {
    @RequestMapping(method = RequestMethod.GET, value = "/api/v1/auth/user/{email}")
    Optional<User> getUserByEmail(@PathVariable("email") String email);

    @RequestMapping(method = RequestMethod.GET,value = "/api/v1/auth/verify")
    TokenClaimsResponse verifyToken();

}
