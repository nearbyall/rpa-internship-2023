package by.fin.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackages = "by.fin.module")
@SpringBootApplication(scanBasePackages = "by.fin")
@EntityScan("by.fin.module.*")
public class RpaApplication {

	public static void main(String[] args) {
		SpringApplication.run(RpaApplication.class, args);
	}

}
