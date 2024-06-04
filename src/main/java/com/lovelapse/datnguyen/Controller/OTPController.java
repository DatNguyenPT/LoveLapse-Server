package com.lovelapse.datnguyen.Controller;

import com.lovelapse.datnguyen.DTO.OTPResponse;
import com.lovelapse.datnguyen.DTO.OTPStatus;
import com.lovelapse.datnguyen.Service.MailOTPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

@RestController
@RequestMapping("/otp")
public class OTPController {
    @Autowired
    private MailOTPService mailOTPService;
    OTPController(MailOTPService mailOTPService){
        this.mailOTPService = mailOTPService;
    }

    @GetMapping(value = "/")
    public String home(){
        return "LOVELAPSE";
    }
    @PostMapping(value = "/sendmailOTP")
    public ResponseEntity<?>sendEmailOTP(@RequestParam String email){
        System.out.println("Sending OTP to email: " + email);
        String otp = mailOTPService.sendOTP(email, "OTP to reset password");
        if (otp != null) {
            return new ResponseEntity<>(otp, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Failed to send OTP", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/validate-emailOTP")
    public ResponseEntity<?> validateEmailOTP(@RequestBody OTPResponse otpResponse) {
        try {
            otpResponse.setStatus(OTPStatus.DELIVERED);
            String email = otpResponse.getMailOrPhone();
            String inputOTP = otpResponse.getMessage();

            System.out.println("Input param: \n");
            System.out.println("Validating OTP for email: " + otpResponse.getMailOrPhone());
            System.out.println("Input OTP: " + otpResponse.getMessage());

            boolean isValid = mailOTPService.validateEmailOTP(email.trim(), inputOTP.trim());

            if (isValid) {
                return ResponseEntity.ok("valid OTP");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid OTP");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Writer writer = new StringWriter();
            e.printStackTrace(new PrintWriter(writer));
            String error = writer.toString();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error validating OTP: " + error);
        }
    }
}
