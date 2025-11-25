package br.restaurante.repository;

import br.restaurante.model.AvaliacaoPrato;
import br.restaurante.model.ItemRestaurante;
import br.restaurante.model.Restaurante;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class AvaliacaoPratoRepositoryTest {

    @Autowired
    private AvaliacaoPratoRepository avaliacaoPratoRepository;

    @Autowired
    private ItemRestauranteRepository itemRestauranteRepository;

    @Autowired
    private RestauranteRepository restauranteRepository;

    @Test
    @DisplayName("Deve salvar avaliação e buscar por ItemRestaurante")
    void deveSalvarEBuscarPorItemRestaurante() {
        // 🔹 Cria e salva o restaurante com CNPJ sem máscara (14 caracteres)
        Restaurante restaurante = new Restaurante();
        restaurante.setNome("Saborê");
        restaurante.setEmail("contato@sabore.com");
        restaurante.setSenha("123456");
        restaurante.setCnpj("12345678000199"); // ✅ valor compatível com @Column(length=14)
        restauranteRepository.save(restaurante);

        // 🔹 Cria e salva o item vinculado ao restaurante
        ItemRestaurante item = new ItemRestaurante();
        item.setNome("Pizza Margherita");
        item.setDescricao("Clássica italiana");
        item.setRestaurante(restaurante);
        itemRestauranteRepository.save(item);

        // 🔹 Cria e salva a avaliação
        AvaliacaoPrato avaliacao = new AvaliacaoPrato();
        avaliacao.setComentario("Excelente!");
        avaliacao.setNota(5.0);
        avaliacao.setItemRestaurante(item);
        avaliacaoPratoRepository.save(avaliacao);

        // 🔹 Busca e valida
        List<AvaliacaoPrato> resultado = avaliacaoPratoRepository.findByItemRestaurante(item);

        assertThat(resultado).isNotEmpty();
        assertThat(resultado.get(0).getComentario()).isEqualTo("Excelente!");
    }

    @Test
    @DisplayName("Deve buscar avaliações pelo ID do item")
    void deveBuscarPorItemRestauranteId() {
        Restaurante restaurante = new Restaurante();
        restaurante.setNome("Restaurante Central");
        restaurante.setEmail("central@sabore.com");
        restaurante.setSenha("abc123");
        restaurante.setCnpj("98765432000100"); // ✅ valor compatível
        restauranteRepository.save(restaurante);

        ItemRestaurante item = new ItemRestaurante();
        item.setNome("Lasanha Bolonhesa");
        item.setDescricao("Com molho artesanal");
        item.setRestaurante(restaurante);
        itemRestauranteRepository.save(item);

        AvaliacaoPrato avaliacao = new AvaliacaoPrato();
        avaliacao.setComentario("Muito saborosa!");
        avaliacao.setNota(4.0);
        avaliacao.setItemRestaurante(item);
        avaliacaoPratoRepository.save(avaliacao);

        List<AvaliacaoPrato> resultado = avaliacaoPratoRepository.findByItemRestauranteId(item.getId());

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getComentario()).isEqualTo("Muito saborosa!");
    }
}
