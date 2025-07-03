package vn.stephen.authservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.stephen.authservice.entity.User;

import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    @Query(nativeQuery = true,value = "CALL AUTH_SERVICE.REGISTER_USER (NULL,NULL,:I_EMAIL, :I_PASSWORD, :I_ROLE, :I_IS_VERIFIED)")
    Object registerUser(
            @Param("I_EMAIL") String email,
            @Param("I_PASSWORD") String password,
            @Param("I_ROLE") String role,
            @Param("I_IS_VERIFIED") Boolean isVerified
    );
}
