package br.restaurante.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class ClienteTest {

    private Cliente cliente;

    @BeforeEach
    void setUp() {
        cliente = new Cliente();
    }

    @Test
    void devePermitirDefinirTodosOsCamposCorretamente() {
        cliente.setId(1L);
        cliente.setNome("Persio Lima");
        cliente.setTelefone("11999999999");
        cliente.setCpf("12345678900");
        cliente.setEmail("persio@email.com");
        cliente.setSenha("senha123");
        cliente.setCep("01234567");
        cliente.setRua("Rua das Flores");
        cliente.setBairro("Centro");
        cliente.setCidade("São Paulo");
        cliente.setEstado("SP");
        cliente.setNumero("100");
        cliente.setAceitaProtecaoDados(true);
        cliente.setAceitaMarketing(false);
        cliente.setAceitaAtendimento(true);

        assertThat(cliente.getId()).isEqualTo(1L);
        assertThat(cliente.getNome()).isEqualTo("Persio Lima");
        assertThat(cliente.getTelefone()).isEqualTo("11999999999");
        assertThat(cliente.getCpf()).isEqualTo("12345678900");
        assertThat(cliente.getEmail()).isEqualTo("persio@email.com");
        assertThat(cliente.getSenha()).isEqualTo("senha123");
        assertThat(cliente.getCep()).isEqualTo("01234567");
        assertThat(cliente.getRua()).isEqualTo("Rua das Flores");
        assertThat(cliente.getBairro()).isEqualTo("Centro");
        assertThat(cliente.getCidade()).isEqualTo("São Paulo");
        assertThat(cliente.getEstado()).isEqualTo("SP");
        assertThat(cliente.getNumero()).isEqualTo("100");
        assertThat(cliente.isAceitaProtecaoDados()).isTrue();
        assertThat(cliente.isAceitaMarketing()).isFalse();
        assertThat(cliente.isAceitaAtendimento()).isTrue();
    }

    @Test
    void devePermitirCriarClienteUsandoConstrutorCompleto() {
        Cliente outro = new Cliente(
                2L, "Maria Oliveira", "11888888888", "98765432100",
                "maria@email.com", "12345", "22222222",
                "Av. Paulista", "Bela Vista", "São Paulo", "SP", "500",
                true, true, false
        );

        assertThat(outro.getId()).isEqualTo(2L);
        assertThat(outro.getNome()).isEqualTo("Maria Oliveira");
        assertThat(outro.getCpf()).isEqualTo("98765432100");
        assertThat(outro.getRua()).contains("Paulista");
        assertThat(outro.isAceitaProtecaoDados()).isTrue();
        assertThat(outro.isAceitaAtendimento()).isFalse();
    }

    @Test
    void devePermitirAtualizarInformacoesDeEndereco() {
        cliente.setRua("Rua Velha");
        cliente.setNumero("10");
        cliente.setCep("11111111");
        cliente.setRua("Rua Nova");
        cliente.setNumero("20");
        cliente.setCep("22222222");

        assertThat(cliente.getRua()).isEqualTo("Rua Nova");
        assertThat(cliente.getNumero()).isEqualTo("20");
        assertThat(cliente.getCep()).isEqualTo("22222222");
    }
}
