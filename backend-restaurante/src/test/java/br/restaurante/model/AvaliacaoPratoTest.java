package br.restaurante.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class AvaliacaoPratoTest {

    private AvaliacaoPrato avaliacaoPrato;
    private Cliente cliente;
    private ItemRestaurante itemRestaurante;

    @BeforeEach
    void setUp() {
        avaliacaoPrato = new AvaliacaoPrato();
        cliente = new Cliente();
        itemRestaurante = new ItemRestaurante();

        cliente.setNome("Persio Lima");
        itemRestaurante.setNome("Filé à Parmegiana");
    }

    @Test
    void devePermitirDefinirTodosOsCamposCorretamente() {
        LocalDateTime data = LocalDateTime.of(2025, 11, 23, 12, 0);

        avaliacaoPrato.setId(1L);
        avaliacaoPrato.setNota(4.9);
        avaliacaoPrato.setComentario("Excelente prato, sabor incrível!");
        avaliacaoPrato.setCliente(cliente);
        avaliacaoPrato.setPrato(itemRestaurante);
        avaliacaoPrato.setDataAvaliacao(data);

        assertThat(avaliacaoPrato.getId()).isEqualTo(1L);
        assertThat(avaliacaoPrato.getNota()).isEqualTo(4.9);
        assertThat(avaliacaoPrato.getComentario()).contains("sabor incrível");
        assertThat(avaliacaoPrato.getCliente().getNome()).isEqualTo("Persio Lima");
        assertThat(avaliacaoPrato.getPrato().getNome()).isEqualTo("Filé à Parmegiana");
        assertThat(avaliacaoPrato.getDataAvaliacao()).isEqualTo(data);
    }

    @Test
    void devePermitirAlterarNotaEComentario() {
        avaliacaoPrato.setNota(3.0);
        avaliacaoPrato.setComentario("Prato ok, mas poderia estar mais quente.");

        assertThat(avaliacaoPrato.getNota()).isEqualTo(3.0);
        assertThat(avaliacaoPrato.getComentario()).contains("mais quente");
    }

    @Test
    void deveAceitarClienteNulo() {
        avaliacaoPrato.setCliente(null);
        assertThat(avaliacaoPrato.getCliente()).isNull();
    }
}
