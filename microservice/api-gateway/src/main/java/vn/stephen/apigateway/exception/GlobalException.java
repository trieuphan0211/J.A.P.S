package vn.stephen.apigateway.exception;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class GlobalException extends RuntimeException{
    private String code;
    private String message;

    public GlobalException(String message) {
        super(message);
    }
}
