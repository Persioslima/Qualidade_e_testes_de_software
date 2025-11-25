package br.restaurante.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ItemPedidoRequestTest {

    @Test
    @DisplayName("Deve setar e retornar corretamente todos os campos do DTO")
    void deveSetarEObterCamposCorretamente() {
        ItemPedidoRequest dto = new ItemPedidoRequest();

        dto.setItemRestauranteId(10L);
        dto.setQuantidade(3);
        dto.setObservacoes("Sem cebola, com queijo extra");
        dto.setIngredientesRemovidos("cebola,tomate");
        dto.setIngredientesAdicionados("queijo,azeitona");

        assertThat(dto.getItemRestauranteId()).isEqualTo(10L);
        assertThat(dto.getQuantidade()).isEqualTo(3);
        assertThat(dto.getObservacoes()).isEqualTo("Sem cebola, com queijo extra");
        assertThat(dto.getIngredientesRemovidos()).contains("cebola");
        assertThat(dto.getIngredientesAdicionados()).contains("queijo");
    }

    @Test
    @DisplayName("Deve permitir campos nulos quando não informados")
    void devePermitirCamposNulos() {
        ItemPedidoRequest dto = new ItemPedidoRequest();

        assertThat(dto.getItemRestauranteId()).isNull();
        assertThat(dto.getQuantidade()).isNull();
        assertThat(dto.getObservacoes()).isNull();
        assertThat(dto.getIngredientesRemovidos()).isNull();
        assertThat(dto.getIngredientesAdicionados()).isNull();
    }
}
