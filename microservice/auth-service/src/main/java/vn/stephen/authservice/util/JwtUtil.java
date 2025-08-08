package vn.stephen.authservice.util;

import jakarta.annotation.PostConstruct;
import org.jose4j.jwa.AlgorithmConstraints;
import org.jose4j.jwk.JsonWebKey;
import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jwk.RsaJwkGenerator;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.consumer.ErrorCodes;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.lang.JoseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import vn.stephen.authservice.constants.ErrorCode;
import vn.stephen.authservice.exception.GlobalException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Locale;

@Component
public class JwtUtil {
    @Value("${app.config.jwt.secret-key}")
    private String SECRET;

    @Value("${app.config.jwt.expires-in}")
    private Integer EXPIRES_IN;

    @Value("${app.config.jwt.refresh-expires-in}")
    private Integer REFRESH_EXPIRES_IN;

    private static final String RSA_KEY_FILE = "rsa.jwk";
    private static final String RSA_REFRESH_KEY_FILE = "rsa-refresh.jwk";

    private final MessageSource messageSource;

    private static RsaJsonWebKey rsaJsonWebKey;
    private static RsaJsonWebKey rsaJsonWebKeyRefreshToken;

    public JwtUtil(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @PostConstruct
    private void init() throws JoseException, IOException {
        rsaJsonWebKey = loadOrCreateKey(RSA_KEY_FILE);
        rsaJsonWebKeyRefreshToken = loadOrCreateKey(RSA_REFRESH_KEY_FILE);
    }

    private RsaJsonWebKey loadOrCreateKey(String filename) throws JoseException, IOException {
        try {
            String keyJson = Files.readString(Paths.get(filename));
            return (RsaJsonWebKey) JsonWebKey.Factory.newJwk(keyJson);  // Cast to RsaJsonWebKey
        } catch (IOException | NullPointerException e) { // Handle file not found or other exceptions
            RsaJsonWebKey key = RsaJwkGenerator.generateJwk(2048);
            Files.writeString(Paths.get(filename), key.toJson(JsonWebKey.OutputControlLevel.INCLUDE_PRIVATE));
            return key;
        }
    }

    // Generate token with given user name
    public String generateToken(String email, String firstName, String lastName, String role, Boolean isRefreshToken) throws JoseException {
        JwtClaims claims = new JwtClaims();
        claims.setIssuer("AUS");
        claims.setAudience("AUS");
        claims.setExpirationTimeMinutesInTheFuture(isRefreshToken ? REFRESH_EXPIRES_IN : EXPIRES_IN);
        claims.setGeneratedJwtId();
        claims.setIssuedAtToNow();
        claims.setNotBeforeMinutesInThePast(2);
        claims.setSubject(email);
        claims.setClaim("firstName", firstName);
        claims.setClaim("lastName", lastName);
        claims.setClaim("role", role);
        return createToken(claims, isRefreshToken);
    }

    // Create a JWT token with specified claims and subject (user name)
    private String createToken(JwtClaims claims, Boolean isRefreshToken) throws JoseException {
        JsonWebSignature jws = new JsonWebSignature();
        jws.setPayload(claims.toJson());
        jws.setKey(getSignKey(isRefreshToken).getPrivateKey());
        jws.setKeyIdHeaderValue(SECRET);
        // Set the signature algorithm on the JWT/JWS that will integrity protect the claims
        jws.setAlgorithmHeaderValue(isRefreshToken ? AlgorithmIdentifiers.RSA_USING_SHA384 : AlgorithmIdentifiers.RSA_USING_SHA256);
        return jws.getCompactSerialization();
    }

    // Get the signing key for JWT token
    private RsaJsonWebKey getSignKey(Boolean isRefreshToken) {
        if (isRefreshToken) {
            rsaJsonWebKeyRefreshToken.setKeyId(SECRET);
            return rsaJsonWebKeyRefreshToken;
        } else {
            rsaJsonWebKey.setKeyId(SECRET);
            return rsaJsonWebKey;
        }
    }

    public JwtClaims jwtConsumer(String token, Boolean isRefreshToken) {
        JwtConsumer jwtConsumer = new JwtConsumerBuilder()
                .setRequireExpirationTime() // the JWT must have an expiration time
                .setAllowedClockSkewInSeconds(isRefreshToken ? REFRESH_EXPIRES_IN : EXPIRES_IN) // allow some leeway in validating time based claims to account for clock skew
                .setRequireSubject() // the JWT must have a subject claim
                .setExpectedIssuer("AUS") // whom the JWT needs to have been issued by
                .setExpectedAudience("AUS") // to whom the JWT is intended for
                .setVerificationKey(getSignKey(isRefreshToken).getPublicKey()) // verify the signature with the public key
                .setJwsAlgorithmConstraints( // only allow the expected signature algorithm(s) in the given context
                        AlgorithmConstraints.ConstraintType.PERMIT,
                        isRefreshToken ?
                                AlgorithmIdentifiers.RSA_USING_SHA384
                                : AlgorithmIdentifiers.RSA_USING_SHA256) // which is only RS256 here
                .build(); // create the JwtConsumer instance
        try {
            //  Validate the JWT and process it to the Claims
            return jwtConsumer.processToClaims(token);
        } catch (InvalidJwtException e) {

            if (e.hasExpired()) {
                throw new GlobalException(
                        ErrorCode.EXPIRED_JWT,
                        messageSource.getMessage("auth.jwt.expired", null,
                                Locale.of("en")));
            }

            // Or maybe the audience was invalid
            if (e.hasErrorCode(ErrorCodes.AUDIENCE_INVALID)) {
                throw new GlobalException(
                        ErrorCode.INVALID_AUDIENCE_JWT,
                        messageSource.getMessage("auth.jwt.audience.wrong", null,
                                Locale.of("en")));
            }
            if (e.hasErrorCode(ErrorCodes.SIGNATURE_INVALID)) {
                throw new GlobalException(
                        ErrorCode.INVALID_SIGNATURE_JWT,
                        messageSource.getMessage("auth.jwt.invalid", null,
                                Locale.of("en")));
            }
            throw new GlobalException(
                    ErrorCode.JOSE_EXCEPTION,
                    e.getMessage());
        }
    }
    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    public String hashPassword(String plainPassword) {
        return encoder.encode(plainPassword);
    }
    public boolean verifyPassword(String plainPassword, String hashedPassword) {
        return encoder.matches(plainPassword, hashedPassword);
    }
}
