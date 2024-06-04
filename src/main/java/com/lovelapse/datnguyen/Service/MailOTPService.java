package com.lovelapse.datnguyen.Service;

import com.lovelapse.datnguyen.DTO.OTPResponse;
import com.lovelapse.datnguyen.DTO.OTPStatus;
import com.lovelapse.datnguyen.Repository.OTPRepo;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Random;

@Service
public class MailOTPService {
    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private OTPRepo otpRepo;

    public OTPResponse validOTP(String foundOTP) {
        return otpRepo.findAll().stream()
                .filter(otp -> otp.getMessage().equals(foundOTP))
                .findFirst()
                .orElse(null);
    }

    public List<OTPResponse> getAll() {
        return otpRepo.findAll();
    }

    public void addOTPToDatabase(String to, OTPResponse newOTP) {
        otpRepo.updateNewOTP(to, newOTP.getMessage());
        otpRepo.save(newOTP);
    }

    public String sendOTP(String to, String subject) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        try {
            helper.setFrom("phamtiendatthcsab123@gmail.com"); // server email
            helper.setTo(to);
            helper.setSubject(subject);

            String otp = generateOTP();
            String body = "Your OTP to reset password: " + otp;

            helper.setText(body, false);
            javaMailSender.send(mimeMessage);

            // Log OTP for debugging
            System.out.println("Generated OTP: " + otp + " for email: " + to);

            OTPResponse newOTP = new OTPResponse();
            newOTP.setMailOrPhone(to);
            newOTP.setMessage(otp);
            newOTP.setStatus(OTPStatus.DELIVERED);

            addOTPToDatabase(to, newOTP);

            System.out.println("Mail sent successfully and OTP stored.");

            return otp;
        } catch (jakarta.mail.MessagingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean validateEmailOTP(String mailOrPhone, String inputOTP) {
        System.out.println("Validating OTP for email: " + mailOrPhone + " with OTP: " + inputOTP);
        OTPResponse target = validOTP(inputOTP);
        if (target != null) {
            int index = mailOrPhone.indexOf("@");
            String targetMailOrPhone = target.getMailOrPhone().substring(0, index);
            String temp = mailOrPhone.substring(0, index);
            if (targetMailOrPhone.trim().equals(temp.trim())){
                System.out.println("Target: " + target.getMailOrPhone() + " - " + target.getMessage() + "\n");
                System.out.println("Valid");

                otpRepo.delete(target);
                return true;
            }

        }
        return false;
    }


    private String generateOTP() {
        return new DecimalFormat("000000").format(new Random().nextInt(999999));
    }
}
