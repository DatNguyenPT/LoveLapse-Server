package com.lovelapse.datnguyen.Controller;

import com.lovelapse.datnguyen.DTO.PhoneRequest;
import com.lovelapse.datnguyen.Service.MailOTPService;
import com.lovelapse.datnguyen.Service.TwilioOTPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        return new ResponseEntity<>(mailOTPService.sendOTP(email, "OTP to reset password"), HttpStatus.OK);
    }

    @PostMapping(value = "/validateemailOTP")
    public ResponseEntity<?>validateEmailOTP(@RequestParam String email, @RequestParam String inputOTP){
        return new ResponseEntity<>(mailOTPService.validateEmailOTP(email, inputOTP), HttpStatus.OK);
    }
}
