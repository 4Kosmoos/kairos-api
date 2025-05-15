package dev.kosmoos.kairosapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(
		scanBasePackages = "dev.kosmoos.kairosapi"
)
@EntityScan("dev.kosmoos.kairosapi.entity")
@EnableJpaRepositories("dev.kosmoos.kairosapi.repository")
public class KairosApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(KairosApiApplication.class, args);
	}

}
