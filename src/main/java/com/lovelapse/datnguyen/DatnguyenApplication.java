package com.lovelapse.datnguyen;

import com.twilio.Twilio;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DatnguyenApplication {
	@Autowired
	private AppConfig appConfig;

	@PostConstruct
	public void initTwilio(){
		Twilio.init(appConfig.getAccountSID(), appConfig.getAuthToken());
	}
	public static void main(String[] args) {
		SpringApplication.run(DatnguyenApplication.class, args);
	}

}

