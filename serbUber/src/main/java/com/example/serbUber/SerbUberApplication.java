package com.example.serbUber;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class SerbUberApplication {

	public static void main(String[] args) {SpringApplication.run(SerbUberApplication.class, args);
	}
}
