package br.restaurante.model;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class NovoEnderecoTest {

    @Test
    void deveConstruirNovoEnderecoAPartirDeEnderecoRecord() {
        Endereco endereco = new Endereco(
                "12345678",
                "Rua das Flores",
                "Centro",
                "São Paulo",
                "SP"
        );

        NovoEndereco novoEndereco = new NovoEndereco(endereco);

        assertThat(novoEndereco.toString()).contains("NovoEndereco");
        assertThat(novoEndereco.toString()).contains("12345678");
        assertThat(novoEndereco.toString()).contains("Rua das Flores");

        // Validação individual
        assertThat(novoEndereco.toString())
                .contains("bairro='Centro'")
                .contains("cidade='São Paulo'")
                .contains("estado='SP'");
    }

    @Test
    void devePermitirConstrutorVazio() {
        NovoEndereco novoEndereco = new NovoEndereco();
        assertThat(novoEndereco).isNotNull();
        assertThat(novoEndereco.toString()).contains("NovoEndereco");
    }

    @Test
    void deveGerarToStringComCamposCorretos() {
        Endereco endereco = new Endereco("99999999", "Rua A", "Bairro B", "Cidade C", "ST");
        NovoEndereco novoEndereco = new NovoEndereco(endereco);

        String texto = novoEndereco.toString();

        assertThat(texto)
                .contains("cep='99999999'")
                .contains("rua='Rua A'")
                .contains("bairro='Bairro B'")
                .contains("cidade='Cidade C'")
                .contains("estado='ST'");
    }

    @Test
    void deveLidarComEnderecoNuloNoConstrutor() {
        NovoEndereco novoEndereco = new NovoEndereco();

        // Não deve lançar exceção, apenas manter campos nulos
        assertThat(novoEndereco.toString())
                .contains("cep='null'")
                .contains("cidade='null'");
    }
}
