package gestionPro.backend.net;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling 
public class GestionProApplication {

	public static void main(String[] args) {
		SpringApplication.run(GestionProApplication.class, args);
	}

}
