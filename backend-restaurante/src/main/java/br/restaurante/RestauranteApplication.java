package br.restaurante;

import br.restaurante.model.Cliente;
import br.restaurante.model.Restaurante;
import br.restaurante.repository.ClienteRepository;
import br.restaurante.repository.RestauranteRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class RestauranteApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestauranteApplication.class, args);
		System.out.println("hello world");
	}

}