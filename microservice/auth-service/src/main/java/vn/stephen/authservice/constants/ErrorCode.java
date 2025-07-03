package vn.stephen.authservice.constants;

public class ErrorCode {
    public static final String UNKNOWN = "FUMS-000";
    public static final String INVALID_JWT = "FUMS-001";
    public static final String NOT_FOUND = "FUMS-002";
    public static final String NULL_DATA = "FUMS-003";
    public static final String MISSING_PARAMETER = "FUMS-004";
    public static final String EXPIRED_JWT = "FUMS-005";
    public static final String INVALID_AUDIENCE_JWT = "FUMS-006";
    public static final String INVALID_SIGNATURE_JWT = "FUMS-007";
    public static final String NULL_POINT_EXCEPTION = "FUMS-008";

    public static final String EXISTED_EMAIL = "FUMS-101";

    public static final String JOSE_INVALID_CREDENTIAL = "FUMS-201";
    public static final String JOSE_EXCEPTION = "FUMS-202";
    public static final String MALFORMED_CLAIM_EXCEPTION = "FUMS-203";
}
