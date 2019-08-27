package demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"demo","controller","config"})
public class S3uploadMicroserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(S3uploadMicroserviceApplication.class, args);
	}
}
