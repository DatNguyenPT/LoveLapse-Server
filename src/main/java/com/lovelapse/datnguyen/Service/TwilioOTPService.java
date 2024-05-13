package com.lovelapse.datnguyen.Service;

import com.lovelapse.datnguyen.AppConfig;
import com.lovelapse.datnguyen.DTO.PhoneRequest;
import com.twilio.Twilio;
import com.twilio.exception.ApiException;
import com.twilio.rest.api.v2010.account.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class TwilioOTPService {
    @Autowired
    private AppConfig appConfig;
    private Map<String, String> matchOTP = new HashMap<>();

    public TwilioOTPService(AppConfig appConfig) {
        this.appConfig = appConfig;
        Twilio.init(appConfig.getAccountSID(), appConfig.getAuthToken());
    }

    // Send OTP
    public String sendOTPForPhone(PhoneRequest request) {
        try {
            String to = request.getNumber();
            String from = appConfig.getTrialNumber();
            String otp = generateOTP();
            String otpMessage = "Your OTP is: " + otp;
            Message message = Message.creator(new com.twilio.type.PhoneNumber(to),
                    new com.twilio.type.PhoneNumber(from), otpMessage).create();

            matchOTP.put(request.getNumber(), otp);

            return otpMessage;
        } catch (ApiException e) {
            return "Failed to send OTP: " + e.getMessage();
        }
    }

    // Validate OTP
    public String validateOTP(String number, String inputOTP) {
        if (inputOTP.equals(matchOTP.get(number))) {
            matchOTP.remove(number);
            return "Valid OTP";
        } else {
            return "Invalid OTP, Please retry";
        }
    }

    // Generate 6 digits OTP
    private String generateOTP() {
        return new DecimalFormat("000000")
                .format(new Random().nextInt(999999));
    }
}
