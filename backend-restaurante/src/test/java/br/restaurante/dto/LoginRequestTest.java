package br.restaurante.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class LoginRequestTest {

    @Test
    @DisplayName("Deve permitir criação e leitura dos campos via construtor completo")
    void deveCriarComConstrutorCompleto() {
        LoginRequest dto = new LoginRequest("teste@email.com", "123456");

        assertThat(dto.getEmail()).isEqualTo("teste@email.com");
        assertThat(dto.getSenha()).isEqualTo("123456");
    }

    @Test
    @DisplayName("Deve permitir criação e leitura dos campos via setters e getters")
    void devePermitirUsoDeSettersEGetters() {
        LoginRequest dto = new LoginRequest();

        dto.setEmail("usuario@sabor.com");
        dto.setSenha("senhaSegura!");

        assertThat(dto.getEmail()).isEqualTo("usuario@sabor.com");
        assertThat(dto.getSenha()).isEqualTo("senhaSegura!");
    }

    @Test
    @DisplayName("Deve permitir campos nulos quando não definidos")
    void devePermitirCamposNulos() {
        LoginRequest dto = new LoginRequest();

        assertThat(dto.getEmail()).isNull();
        assertThat(dto.getSenha()).isNull();
    }
}
