package hu.kissgabor.surveystats;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class SurveystatsApplication {

	public static void main(String[] args) {
		SpringApplication.run(SurveystatsApplication.class, args);
	}

}