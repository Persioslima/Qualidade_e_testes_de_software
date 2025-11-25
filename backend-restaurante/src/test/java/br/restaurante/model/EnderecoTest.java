package br.restaurante.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class EnderecoTest {

    @Test
    void deveCriarEnderecoCorretamente() {
        Endereco endereco = new Endereco(
                "12345678",
                "Rua das Flores",
                "Centro",
                "São Paulo",
                "SP"
        );

        assertThat(endereco.cep()).isEqualTo("12345678");
        assertThat(endereco.rua()).isEqualTo("Rua das Flores");
        assertThat(endereco.bairro()).isEqualTo("Centro");
        assertThat(endereco.cidade()).isEqualTo("São Paulo");
        assertThat(endereco.estado()).isEqualTo("SP");
    }

    @Test
    void deveGerarToStringComFormatoEsperado() {
        Endereco endereco = new Endereco("12345678", "Rua A", "Bairro B", "Cidade C", "ST");

        String texto = endereco.toString();

        assertThat(texto)
                .contains("cep='12345678'")
                .contains("rua='Rua A'")
                .contains("bairro='Bairro B'")
                .contains("cidade='Cidade C'")
                .contains("estado='ST'");
    }

    @Test
    void deveDeserializarJsonComAliasesCorretamente() throws Exception {
        String json = """
                {
                  "cep": "01001000",
                  "logradouro": "Praça da Sé",
                  "bairro": "Sé",
                  "localidade": "São Paulo",
                  "estado": "SP"
                }
                """;

        ObjectMapper mapper = new ObjectMapper();
        Endereco endereco = mapper.readValue(json, Endereco.class);

        assertThat(endereco.cep()).isEqualTo("01001000");
        assertThat(endereco.rua()).isEqualTo("Praça da Sé");
        assertThat(endereco.bairro()).isEqualTo("Sé");
        assertThat(endereco.cidade()).isEqualTo("São Paulo");
        assertThat(endereco.estado()).isEqualTo("SP");
    }

    @Test
    void deveIgnorarCamposDesconhecidosNoJson() throws Exception {
        String json = """
                {
                  "cep": "22222222",
                  "logradouro": "Rua das Palmeiras",
                  "bairro": "Botafogo",
                  "localidade": "Rio de Janeiro",
                  "estado": "RJ",
                  "pais": "Brasil",
                  "populacao": 2000000
                }
                """;

        ObjectMapper mapper = new ObjectMapper();
        Endereco endereco = mapper.readValue(json, Endereco.class);

        assertThat(endereco.rua()).isEqualTo("Rua das Palmeiras");
        assertThat(endereco.cidade()).isEqualTo("Rio de Janeiro");
        assertThat(endereco.estado()).isEqualTo("RJ");
    }
}

