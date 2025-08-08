package vn.stephen.apigateway.dto;

import lombok.*;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class TokenClaimsResponse {
    private Map<String, Object> claimsMap;
    private String rawJson;
    private String subject;
    private NumericDateDto expirationTime;
    private String issuer;
    private NumericDateDto notBefore;
    private List<String> audience;
    private String jwtId;
    private NumericDateDto issuedAt;
    private List<String> claimNames;
}

@Getter
@Setter
@ToString
class NumericDateDto {
    private long value;
    private long valueInMillis;
}
