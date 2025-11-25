package br.restaurante.repository;

import br.restaurante.model.Avaliacao;
import br.restaurante.model.Restaurante;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class AvaliacaoRepositoryTest {

    @Autowired
    private AvaliacaoRepository avaliacaoRepository;

    @Autowired
    private RestauranteRepository restauranteRepository;

    @Test
    @DisplayName("Deve salvar e buscar avaliações por restaurante")
    void deveBuscarPorRestaurante() {
        Restaurante restaurante = new Restaurante();
        restaurante.setNome("Cantina Bella Napoli");
        restaurante.setEmail("contato@napoli.com");
        restaurante.setSenha("123");
        restaurante.setCnpj("11111111000111");
        restauranteRepository.save(restaurante);

        Avaliacao a1 = new Avaliacao();
        a1.setNota(4.5);
        a1.setComentario("Excelente atendimento");
        a1.setRestaurante(restaurante);

        Avaliacao a2 = new Avaliacao();
        a2.setNota(3.0);
        a2.setComentario("Bom custo-benefício");
        a2.setRestaurante(restaurante);

        avaliacaoRepository.save(a1);
        avaliacaoRepository.save(a2);

        List<Avaliacao> resultado = avaliacaoRepository.findByRestaurante(restaurante);

        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).getRestaurante().getNome()).isEqualTo("Cantina Bella Napoli");
    }

    @Test
    @DisplayName("Deve buscar avaliações por restaurante e nota específica")
    void deveBuscarPorRestauranteENota() {
        Restaurante restaurante = new Restaurante();
        restaurante.setNome("Pizzaria Central");
        restaurante.setEmail("central@pizza.com");
        restaurante.setSenha("abc123");
        restaurante.setCnpj("22222222000122");
        restauranteRepository.save(restaurante);

        Avaliacao a1 = new Avaliacao();
        a1.setNota(5.0);
        a1.setComentario("Perfeita!");
        a1.setRestaurante(restaurante);

        Avaliacao a2 = new Avaliacao();
        a2.setNota(4.0);
        a2.setComentario("Boa, mas poderia ser mais quente");
        a2.setRestaurante(restaurante);

        avaliacaoRepository.save(a1);
        avaliacaoRepository.save(a2);

        List<Avaliacao> resultado = avaliacaoRepository.findByRestauranteAndNota(restaurante, 5.0);

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getComentario()).isEqualTo("Perfeita!");
    }

    @Test
    @DisplayName("Deve buscar avaliações com nota mínima especificada")
    void deveBuscarPorNotaMinima() {
        Restaurante restaurante = new Restaurante();
        restaurante.setNome("Restaurante do Chef");
        restaurante.setEmail("chef@sabor.com");
        restaurante.setSenha("xyz");
        restaurante.setCnpj("33333333000133");
        restauranteRepository.save(restaurante);

        Avaliacao a1 = new Avaliacao();
        a1.setNota(3.0);
        a1.setComentario("Ok");
        a1.setRestaurante(restaurante);

        Avaliacao a2 = new Avaliacao();
        a2.setNota(4.0);
        a2.setComentario("Muito bom");
        a2.setRestaurante(restaurante);

        Avaliacao a3 = new Avaliacao();
        a3.setNota(5.0);
        a3.setComentario("Excelente!");
        a3.setRestaurante(restaurante);

        avaliacaoRepository.saveAll(List.of(a1, a2, a3));

        List<Avaliacao> resultado = avaliacaoRepository.findByNotaGreaterThanEqual(4.0);

        assertThat(resultado).hasSize(2);
        assertThat(resultado).extracting(Avaliacao::getNota).containsExactlyInAnyOrder(4.0, 5.0);
    }
}
