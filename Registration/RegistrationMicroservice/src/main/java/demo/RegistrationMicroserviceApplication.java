package demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan({"demo","controller","config"})
@SpringBootApplication
public class RegistrationMicroserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(RegistrationMicroserviceApplication.class, args);
	}
}
