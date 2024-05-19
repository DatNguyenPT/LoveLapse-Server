package com.lovelapse.datnguyen.Controller;

import com.lovelapse.datnguyen.DTO.PhoneRequest;
import com.lovelapse.datnguyen.Service.MailOTPService;
import com.lovelapse.datnguyen.Service.TwilioOTPService;
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
    private TwilioOTPService twilioOTPService;

    @Autowired
    private MailOTPService mailOTPService;

    OTPController(TwilioOTPService twilioOTPService){
        this.twilioOTPService = twilioOTPService;
    }

    @GetMapping(value = "/")
    public String home(){
        return "LOVELAPSE";
    }

    @PostMapping(value = "/sendOTP")
    public ResponseEntity<?>sendOTP(@RequestBody PhoneRequest request){
        return new ResponseEntity<>(twilioOTPService.sendOTPForPhone(request), HttpStatus.OK);
    }

    @PostMapping(value = "/validateOTP")
    public ResponseEntity<?>validateOTP(@RequestBody PhoneRequest request){
        return new ResponseEntity<>(twilioOTPService.validateOTP(request.getNumber(), request.getOtp()), HttpStatus.OK);
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
    public ResponseEntity<?> validateEmailOTP(@RequestParam String email, @RequestParam String inputOTP) {
        try {
            System.out.println("Validating OTP for email: " + email);
            System.out.println("Input OTP: " + inputOTP);

            boolean isValid = mailOTPService.validateEmailOTP(email, inputOTP.trim());
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
