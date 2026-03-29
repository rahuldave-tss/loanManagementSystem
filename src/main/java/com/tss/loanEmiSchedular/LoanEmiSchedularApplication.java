package com.tss.loanEmiSchedular;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@EnableAsync
@EnableScheduling
@SpringBootApplication
@EnableMethodSecurity
public class LoanEmiSchedularApplication {

	public static void main(String[] args) {
		SpringApplication.run(LoanEmiSchedularApplication.class, args);
	}

}
