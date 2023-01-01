package ru.job4j.accidents;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class AccidentsApplication {

	public static void main(String[] args) {
		SpringApplication.run(AccidentsApplication.class, args);
		System.out.println("Go to http://localhost:8080/");
	}
}