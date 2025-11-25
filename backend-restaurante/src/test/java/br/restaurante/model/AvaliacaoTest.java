package br.restaurante.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class AvaliacaoTest {

    private Avaliacao avaliacao;
    private Cliente cliente;
    private Restaurante restaurante;

    @BeforeEach
    void setUp() {
        avaliacao = new Avaliacao();
        cliente = new Cliente();
        restaurante = new Restaurante();

        cliente.setNome("Persio Lima");
        restaurante.setNome("Sabore Grill");
    }

    @Test
    void deveDefinirDataAvaliacaoAutomaticamenteAntesDePersistir() {
        avaliacao.prePersist();
        assertThat(avaliacao.getDataAvaliacao()).isNotNull();
        assertThat(avaliacao.getDataAvaliacao()).isBeforeOrEqualTo(LocalDateTime.now());
    }

    @Test
    void devePermitirDefinirTodosOsCamposCorretamente() {
        avaliacao.setId(1L);
        avaliacao.setNota(4.8);
        avaliacao.setComentario("Comida excelente!");
        avaliacao.setCliente(cliente);
        avaliacao.setRestaurante(restaurante);
        avaliacao.setDataAvaliacao(LocalDateTime.of(2025, 11, 23, 12, 0));

        assertThat(avaliacao.getId()).isEqualTo(1L);
        assertThat(avaliacao.getNota()).isEqualTo(4.8);
        assertThat(avaliacao.getComentario()).contains("excelente");
        assertThat(avaliacao.getCliente().getNome()).isEqualTo("Persio Lima");
        assertThat(avaliacao.getRestaurante().getNome()).isEqualTo("Sabore Grill");
        assertThat(avaliacao.getDataAvaliacao()).isEqualTo(LocalDateTime.of(2025, 11, 23, 12, 0));
    }
}
