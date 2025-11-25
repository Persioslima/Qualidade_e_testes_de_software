package br.restaurante.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RestauranteTest {

    private Restaurante restaurante;

    @BeforeEach
    void setup() {
        restaurante = new Restaurante();
        restaurante.setNome("Pizzaria Napoli");
        restaurante.setEmail("contato@napoli.com");
    }

    @Test
    @DisplayName("toString não deve retornar a senha")
    void toStringNaoDeveRetornarSenha() {
        restaurante.setSenha("123456");

        String texto = restaurante.toString();

        System.out.println("\n===== DEBUG toString() =====");
        System.out.println(texto);
        System.out.println("============================\n");

        boolean contemSenha = texto.contains("123456");
        assertThat(contemSenha)
                .as("O método toString() não deve conter a senha")
                .isFalse();
    }
}
