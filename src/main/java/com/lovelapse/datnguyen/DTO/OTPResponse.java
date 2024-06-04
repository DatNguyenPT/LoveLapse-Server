package com.lovelapse.datnguyen.DTO;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name="otp")
public class OTPResponse {
    @Id
    @Column(name="mailorphone")
    private String mailOrPhone;

    @Column(name="mess")
    private String message;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private OTPStatus status;
}
