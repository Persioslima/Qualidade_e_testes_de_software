package br.restaurante.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class PedidoTest {

    private Pedido pedido;
    private Cliente cliente;
    private Restaurante restaurante;
    private ItemPedido item1;
    private ItemPedido item2;

    @BeforeEach
    void setUp() {
        cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNome("Persio Lima");

        restaurante = new Restaurante();
        restaurante.setId(10L);
        restaurante.setNome("Restaurante Sabore");

        pedido = Pedido.builder()
                .id(100L)
                .cliente(cliente)
                .restaurante(restaurante)
                .status("EM_PREPARO")
                .observacoesGerais("Sem cebola")
                .build();

        item1 = new ItemPedido();
        item1.setQuantidade(2);

        item2 = new ItemPedido();
        item2.setQuantidade(1);
    }

    @Test
    void deveInicializarComValoresPadraoAoPersistir() {
        Pedido novoPedido = new Pedido();
        novoPedido.prePersist();

        assertThat(novoPedido.getStatus()).isEqualTo("NOVO");
        assertThat(novoPedido.getCriadoEm()).isNotNull();
    }

    @Test
    void deveAdicionarEManterRelacionamentoBidirecional() {
        pedido.adicionarItem(item1);
        pedido.adicionarItem(item2);

        assertThat(pedido.getItens()).hasSize(2);
        assertThat(item1.getPedido()).isEqualTo(pedido);
        assertThat(item2.getPedido()).isEqualTo(pedido);
    }

    @Test
    void deveRemoverItemDoPedidoComSeguranca() {
        pedido.adicionarItem(item1);
        pedido.removerItem(item1);

        assertThat(pedido.getItens()).isEmpty();
        assertThat(item1.getPedido()).isNull();
    }

    @Test
    void deveGerarToStringComDadosEssenciais() {
        String texto = pedido.toString();

        assertThat(texto)
                .contains("Pedido{id=")
                .contains("cliente=1")
                .contains("restaurante=10")
                .contains("status='EM_PREPARO'");
    }

    @Test
    void equalsDeveCompararApenasPorId() {
        Pedido outro = new Pedido();
        outro.setId(100L);

        assertThat(pedido).isEqualTo(outro);
        outro.setId(200L);
        assertThat(pedido).isNotEqualTo(outro);
    }

    @Test
    void hashCodeDeveSerBaseadoNaClasse() {
        int hash1 = pedido.hashCode();
        int hash2 = new Pedido().hashCode();

        assertThat(hash1).isEqualTo(hash2);
    }

    @Test
    void devePermitirAtualizarStatusEObservacoes() {
        pedido.setStatus("CONCLUIDO");
        pedido.setObservacoesGerais("Pedido entregue com sucesso");

        assertThat(pedido.getStatus()).isEqualTo("CONCLUIDO");
        assertThat(pedido.getObservacoesGerais()).contains("entregue");
    }
}
