package vn.stephen.authservice.dto;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Remark {
    private String language_name;
    private String remark_message;
}
