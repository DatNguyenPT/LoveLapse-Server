package com.lovelapse.datnguyen.Service;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class MailOTPService {
    @Autowired
    private JavaMailSender javaMailSender;
    private final Map<String, String> otpMap = new HashMap<>();

    public String sendOTP(String to, String subject) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);

        try {
            helper.setFrom("phamtiendatthcsab123@gmail.com");
            helper.setTo(to);
            helper.setSubject(subject);

            String otp = generateOTP();
            String body = "Your OTP to reset password: " + otp;

            helper.setText(body, false);
            javaMailSender.send(mimeMessage);

            // Log OTP for debugging
            System.out.println("Generated OTP: " + otp + " for email: " + to);

            synchronized (otpMap) {
                otpMap.put(to, otp);
            }

            System.out.println("Mail sent successfully and OTP stored.");

            return otp;
        } catch (jakarta.mail.MessagingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean validateEmailOTP(String to, String inputOTP) {
        System.out.println("Validating OTP for email: " + to + " with OTP: " + inputOTP);
        synchronized (otpMap) {
            String storedOtp = otpMap.get(to);
            System.out.println("Stored OTP: " + storedOtp);
            if (storedOtp != null && storedOtp.equals(inputOTP)) {
                otpMap.remove(to);
                System.out.println("OTP is valid");
                return true;
            } else {
                System.out.println("Invalid OTP");
                return false;
            }
        }
    }

    private String generateOTP() {
        return new DecimalFormat("000000").format(new Random().nextInt(999999));
    }
}
