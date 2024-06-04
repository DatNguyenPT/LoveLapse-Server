package com.lovelapse.datnguyen;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DatnguyenApplication {
	@Autowired
	private AppConfig appConfig;
	public static void main(String[] args) {
		SpringApplication.run(DatnguyenApplication.class, args);
	}

}

