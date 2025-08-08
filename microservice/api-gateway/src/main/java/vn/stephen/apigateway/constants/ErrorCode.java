package vn.stephen.apigateway.constants;

public class ErrorCode {
    public static final String UNKNOWN = "AUS-000";
    public static final String INVALID_JWT = "AUS-001";
    public static final String NOT_FOUND = "AUS-002";
    public static final String NULL_DATA = "AUS-003";
    public static final String MISSING_PARAMETER = "AUS-004";
    public static final String EXPIRED_JWT = "AUS-005";
    public static final String INVALID_AUDIENCE_JWT = "AUS-006";
    public static final String INVALID_SIGNATURE_JWT = "AUS-007";
    public static final String NULL_POINT_EXCEPTION = "AUS-008";

    public static final String EXISTED_EMAIL = "AUS-101";

    public static final String JOSE_INVALID_CREDENTIAL = "AUS-201";
    public static final String JOSE_EXCEPTION = "AUS-202";
    public static final String MALFORMED_CLAIM_EXCEPTION = "AUS-203";
}
