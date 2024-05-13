package com.lovelapse.datnguyen.DTO;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PhoneOTPResponse {
    private OTPStatus status;
    private String message;
}
