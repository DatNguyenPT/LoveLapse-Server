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
    private Map<String, String>otpMap = new HashMap<>();

    public String sendOTP(String to, String subject) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);

        try {
            helper.setFrom("phamtiendatthcsab123@gmail.com");
            helper.setTo(to);
            helper.setSubject(subject);

            String otp = generateOTP();
            String body = "Your OTP to reset password: " + otp;

            // Set the body of the email as HTML content
            helper.setText(body, false);

            javaMailSender.send(mimeMessage);

            System.out.println("Mail sent successfully");

            otpMap.put(to, otp);

            return otp;
        } catch (jakarta.mail.MessagingException e) {
            // Handle messaging exception
            e.printStackTrace();
            return null;
        }
    }

    public String validateEmailOTP(String to, String inputOTP){
        if (otpMap.get(to).equals(inputOTP)){
            otpMap.remove(to, inputOTP);
            return "valid OTP";
        }else{
            return "invalid OTP";
        }
    }

    private String generateOTP() {
        return new DecimalFormat("000000").format(new Random().nextInt(999999));
    }
}
