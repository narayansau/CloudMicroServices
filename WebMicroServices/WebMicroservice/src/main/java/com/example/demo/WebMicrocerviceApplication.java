package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"demo","controller","config"})
public class WebMicrocerviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebMicrocerviceApplication.class, args);
	}
}
