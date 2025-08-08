package vn.stephen.authservice.rest;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import vn.stephen.authservice.dto.UserRequest;

import java.util.Map;

@FeignClient(name = "user-service")
public interface UserServiceRest {
    @RequestMapping(method = RequestMethod.GET, value = "/api/v1/user/create")
    Map<String, Object> createUser(UserRequest userRequest);

}
