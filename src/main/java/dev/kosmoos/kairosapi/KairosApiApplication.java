package dev.kosmoos.kairosapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class KairosApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(KairosApiApplication.class, args);
	}

	@GetMapping("/ping")
	public String home() {
		return "KairosApi Pong!";
	}

}
