package br.restaurante.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

public class ItemRestauranteTest {

    private ItemRestaurante item;
    private Restaurante restaurante;
    private AvaliacaoPrato avaliacao1;
    private AvaliacaoPrato avaliacao2;

    @BeforeEach
    void setUp() {
        item = new ItemRestaurante();
        restaurante = new Restaurante();
        restaurante.setNome("Restaurante do Persio");

        avaliacao1 = new AvaliacaoPrato();
        avaliacao1.setComentario("Excelente prato!");
        avaliacao1.setNota(5.0);

        avaliacao2 = new AvaliacaoPrato();
        avaliacao2.setComentario("Muito bom!");
        avaliacao2.setNota(4.5);
    }

    @Test
    void devePermitirDefinirTodosOsCamposCorretamente() {
        item.setId(1L);
        item.setNome("Pizza Calabresa");
        item.setDescricao("Calabresa artesanal com cebola roxa e queijo");
        item.setPreco(39.90);
        item.setImagemUrl("https://cdn.exemplo.com/pizza.jpg");
        item.setRestaurante(restaurante);

        assertThat(item.getId()).isEqualTo(1L);
        assertThat(item.getNome()).isEqualTo("Pizza Calabresa");
        assertThat(item.getDescricao()).contains("Calabresa artesanal");
        assertThat(item.getPreco()).isEqualTo(39.90);
        assertThat(item.getImagemUrl()).contains("pizza.jpg");
        assertThat(item.getRestaurante().getNome()).isEqualTo("Restaurante do Persio");
    }

    @Test
    void devePermitirAssociarAvaliacoesAoItem() {
        item.setAvaliacoes(List.of(avaliacao1, avaliacao2));

        assertThat(item.getAvaliacoes())
                .hasSize(2)
                .extracting(AvaliacaoPrato::getNota)
                .contains(5.0, 4.5);
    }

    @Test
    void devePermitirAtualizarInformacoesDePrecoEImagem() {
        item.setPreco(25.00);
        item.setImagemUrl("https://cdn.exemplo.com/antiga.jpg");

        item.setPreco(29.90);
        item.setImagemUrl("https://cdn.exemplo.com/nova.jpg");

        assertThat(item.getPreco()).isEqualTo(29.90);
        assertThat(item.getImagemUrl()).contains("nova.jpg");
    }

    @Test
    void deveAceitarDescricaoENomeVazios() {
        item.setNome("");
        item.setDescricao("");

        assertThat(item.getNome()).isEmpty();
        assertThat(item.getDescricao()).isEmpty();
    }

    @Test
    void deveAceitarRestauranteNulo() {
        item.setRestaurante(null);
        assertThat(item.getRestaurante()).isNull();
    }
}
