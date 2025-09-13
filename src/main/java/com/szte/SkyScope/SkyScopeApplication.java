package com.szte.SkyScope;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class SkyScopeApplication {

	public static void main(String[] args) {
		SpringApplication.run(SkyScopeApplication.class, args);
	}

}
