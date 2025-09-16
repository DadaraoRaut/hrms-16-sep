package com.quantumsoft.hrms.Human_Resource_Website;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class HumanResourceWebsiteApplication {

	public static void main(String[] args) {
		SpringApplication.run(HumanResourceWebsiteApplication.class, args);
	}

}
