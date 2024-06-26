package com.lovelapse.datnguyen;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
@ComponentScan(basePackages = "com.lovelapse.datnguyen")
public class AppConfig {

    @Autowired
    private Environment env;

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(env.getProperty("spring.mail.host"));
        mailSender.setPort(Integer.parseInt(env.getProperty("spring.mail.port")));
        mailSender.setUsername(env.getProperty("spring.mail.username"));
        mailSender.setPassword(env.getProperty("spring.mail.password"));

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        return mailSender;
    }

    @Value("${cloudinary.cloud-name}")
    private String cloudName;

    @Value("${cloudinary.api-key}")
    private String apiKey;

    @Value("${cloudinary.api-secret}")
    private String apiSecret;

    @Bean
    public Cloudinary getCloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret
        ));
    }

    private FirebaseApp firebaseApp;

    @Bean
    public FirebaseDatabase firebaseDatabase() throws Exception {
        if (firebaseApp == null) {
            initializeFirebaseApp();
        }
        return FirebaseDatabase.getInstance(firebaseApp);
    }

    @Bean
    public FirebaseMessaging firebaseMessaging() throws Exception {
        if (firebaseApp == null) {
            initializeFirebaseApp();
        }
        return FirebaseMessaging.getInstance(firebaseApp);
    }

    private synchronized void initializeFirebaseApp() throws Exception {
        if (firebaseApp == null) {
            GoogleCredentials googleCredentials = GoogleCredentials.fromStream(
                    new ClassPathResource("google-services.json").getInputStream());
            FirebaseOptions firebaseOptions = FirebaseOptions.builder()
                    .setCredentials(googleCredentials)
                    .setDatabaseUrl("https://tinderv2-92c85-default-rtdb.asia-southeast1.firebasedatabase.app/")
                    .build();
            firebaseApp = FirebaseApp.initializeApp(firebaseOptions, "lovelapse-server");
        }
    }
}
