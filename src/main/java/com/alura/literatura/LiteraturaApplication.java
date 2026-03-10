package com.alura.literatura;

import com.alura.literatura.principal.Principal;
import com.alura.literatura.repository.AutorRepository;
import com.alura.literatura.repository.LibroRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LiteraturaApplication {

	public static void main(String[] args) {

		var context = SpringApplication.run(LiteraturaApplication.class, args);

		AutorRepository autorRepository = context.getBean(AutorRepository.class);
		LibroRepository libroRepository = context.getBean(LibroRepository.class);

		Principal principal = new Principal(autorRepository, libroRepository);
		principal.muestraMenu();
	}
}