package com.example.seugoi_back;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class SeugoiBackApplication {

	public static void main(String[] args) {
		SpringApplication.run(SeugoiBackApplication.class, args);
	}

}
