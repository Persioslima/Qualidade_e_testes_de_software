package br.restaurante;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Teste de inicialização do contexto principal da aplicação.
 * 
 * Garante que o Spring Boot consegue subir o contexto sem falhas.
 */
@SpringBootTest
@ActiveProfiles("test") // 🔹 Usa o perfil de teste com H2
class RestauranteApplicationTest {

    @Test
    void contextLoads() {
        // Se o contexto subir sem exceções, o teste passa.
    }
}
