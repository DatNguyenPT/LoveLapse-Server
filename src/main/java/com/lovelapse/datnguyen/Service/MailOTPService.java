package com.lovelapse.datnguyen.Service;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.text.DecimalFormat;
import java.util.Random;

@Service
public class MailOTPService {
    @Autowired
    private JavaMailSender javaMailSender;

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

            return otp;
        } catch (jakarta.mail.MessagingException e) {
            // Handle messaging exception
            e.printStackTrace();
            return null;
        }
    }

    private String generateOTP() {
        return new DecimalFormat("000000").format(new Random().nextInt(999999));
    }
}
