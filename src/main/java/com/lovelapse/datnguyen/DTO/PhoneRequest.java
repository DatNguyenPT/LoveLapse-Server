package com.lovelapse.datnguyen.DTO;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PhoneRequest {
    private String number;
    private String otp;
}
