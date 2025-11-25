package br.restaurante.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PedidoRequestTest {

    @Test
    @DisplayName("Deve setar e retornar corretamente todos os campos do DTO PedidoRequest")
    void deveSetarEObterCamposCorretamente() {
        ItemPedidoRequest item1 = new ItemPedidoRequest();
        item1.setItemRestauranteId(1L);
        item1.setQuantidade(2);
        item1.setObservacoes("Sem cebola");

        PedidoRequest dto = new PedidoRequest();
        dto.setRestauranteId(10L);
        dto.setItens(List.of(item1));
        dto.setObservacoesGerais("Entrega rápida, por favor");

        assertThat(dto.getRestauranteId()).isEqualTo(10L);
        assertThat(dto.getItens()).hasSize(1);
        assertThat(dto.getItens().get(0).getItemRestauranteId()).isEqualTo(1L);
        assertThat(dto.getObservacoesGerais()).isEqualTo("Entrega rápida, por favor");
    }

    @Test
    @DisplayName("Deve permitir campos nulos quando não informados")
    void devePermitirCamposNulos() {
        PedidoRequest dto = new PedidoRequest();

        assertThat(dto.getRestauranteId()).isNull();
        assertThat(dto.getItens()).isNull();
        assertThat(dto.getObservacoesGerais()).isNull();
    }

    @Test
    @DisplayName("Deve aceitar lista vazia de itens sem lançar exceção")
    void deveAceitarListaVaziaDeItens() {
        PedidoRequest dto = new PedidoRequest();
        dto.setItens(List.of());

        assertThat(dto.getItens()).isEmpty();
    }
}
