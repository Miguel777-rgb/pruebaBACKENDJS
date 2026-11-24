package com.maki.pompompurin;

import com.maki.pompompurin.model.Maki;
import com.maki.pompompurin.repository.MakiRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class PompompurinApplication {

	public static void main(String[] args) {
		SpringApplication.run(PompompurinApplication.class, args);
	}

	// Al iniciar, si la tabla está vacía, carga dos makis de ejemplo
	@Bean
	CommandLineRunner cargarDatosIniciales(MakiRepository makiRepository) {
		return args -> {
			if (makiRepository.count() == 0) {
				makiRepository.save(new Maki("Acevichado", 25.0, 10, "Langostino, palta, salsa acevichada", "Grande"));
				makiRepository.save(new Maki("Furai", 22.0, 10, "Langostino empanizado, queso crema", "Pequeño"));
			}
		};
	}

}
