package br.restaurante.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class ItemPedidoTest {

    private ItemPedido itemPedido;
    private Pedido pedido;
    private ItemRestaurante itemRestaurante;

    @BeforeEach
    void setUp() {
        itemPedido = new ItemPedido();
        pedido = new Pedido();
        itemRestaurante = new ItemRestaurante();

        // simulando dados simples de dependências
        itemRestaurante.setNome("Pizza Margherita");
        pedido.setStatus("NOVO");
    }

    @Test
    void devePermitirDefinirTodosOsCamposCorretamente() {
        itemPedido.setPedido(pedido);
        itemPedido.setItemRestaurante(itemRestaurante);
        itemPedido.setQuantidade(2);
        itemPedido.setObservacoes("Sem cebola");
        itemPedido.setIngredientesRemovidos("Cebola, alho");
        itemPedido.setIngredientesAdicionados("Queijo extra");

        assertThat(itemPedido.getPedido()).isEqualTo(pedido);
        assertThat(itemPedido.getItemRestaurante()).isEqualTo(itemRestaurante);
        assertThat(itemPedido.getQuantidade()).isEqualTo(2);
        assertThat(itemPedido.getObservacoes()).isEqualTo("Sem cebola");
        assertThat(itemPedido.getIngredientesRemovidos()).contains("Cebola");
        assertThat(itemPedido.getIngredientesAdicionados()).isEqualTo("Queijo extra");
    }

    @Test
    void devePermitirAtualizarQuantidadeEObservacoes() {
        itemPedido.setQuantidade(1);
        itemPedido.setObservacoes("Bem passado");

        assertThat(itemPedido.getQuantidade()).isEqualTo(1);
        assertThat(itemPedido.getObservacoes()).isEqualTo("Bem passado");

        itemPedido.setQuantidade(3);
        itemPedido.setObservacoes("Sem sal");

        assertThat(itemPedido.getQuantidade()).isEqualTo(3);
        assertThat(itemPedido.getObservacoes()).isEqualTo("Sem sal");
    }

    @Test
    void deveAceitarCamposDeIngredientesNulosOuVazios() {
        itemPedido.setIngredientesRemovidos(null);
        itemPedido.setIngredientesAdicionados("");

        assertThat(itemPedido.getIngredientesRemovidos()).isNull();
        assertThat(itemPedido.getIngredientesAdicionados()).isEmpty();
    }

    @Test
    void deveManterRelacoesDePedidoEItemRestaurante() {
        itemPedido.setPedido(pedido);
        itemPedido.setItemRestaurante(itemRestaurante);

        assertThat(itemPedido.getPedido().getStatus()).isEqualTo("NOVO");
        assertThat(itemPedido.getItemRestaurante().getNome()).isEqualTo("Pizza Margherita");
    }
}
