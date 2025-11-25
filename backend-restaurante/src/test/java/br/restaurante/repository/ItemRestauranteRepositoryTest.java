package br.restaurante.repository;

import br.restaurante.model.ItemRestaurante;
import br.restaurante.model.Restaurante;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ItemRestauranteRepositoryTest {

    @Autowired
    private ItemRestauranteRepository itemRestauranteRepository;

    @Autowired
    private RestauranteRepository restauranteRepository;

    @Test
    @DisplayName("Deve buscar itens por restaurante e ordenar pelo nome")
    void deveBuscarPorRestauranteOrdenadoPorNome() {
        Restaurante restaurante = new Restaurante();
        restaurante.setNome("Restaurante Tropical");
        restaurante.setEmail("contato@tropical.com");
        restaurante.setSenha("123");
        restaurante.setCnpj("11111111000111");
        restauranteRepository.save(restaurante);

        ItemRestaurante item1 = new ItemRestaurante();
        item1.setNome("Bolo de Chocolate");
        item1.setDescricao("Com cobertura de brigadeiro");
        item1.setPreco(10.0);
        item1.setRestaurante(restaurante);

        ItemRestaurante item2 = new ItemRestaurante();
        item2.setNome("Arroz à Grega");
        item2.setDescricao("Arroz com legumes");
        item2.setPreco(15.0);
        item2.setRestaurante(restaurante);

        itemRestauranteRepository.saveAll(List.of(item1, item2));

        List<ItemRestaurante> resultado = itemRestauranteRepository.findByRestauranteIdOrderByNomeAsc(restaurante.getId());

        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).getNome()).isEqualTo("Arroz à Grega"); // vem antes alfabeticamente
        assertThat(resultado.get(1).getNome()).isEqualTo("Bolo de Chocolate");
    }

    @Test
    @DisplayName("Deve buscar itens pelo nome ignorando maiúsculas e minúsculas")
    void deveBuscarPorNomeIgnoreCase() {
        Restaurante restaurante = new Restaurante();
        restaurante.setNome("Sabores do Chef");
        restaurante.setEmail("chef@sabores.com");
        restaurante.setSenha("xyz");
        restaurante.setCnpj("22222222000122");
        restauranteRepository.save(restaurante);

        ItemRestaurante item = new ItemRestaurante();
        item.setNome("Feijoada");
        item.setDescricao("Completa com couve e laranja");
        item.setPreco(25.0);
        item.setRestaurante(restaurante);
        itemRestauranteRepository.save(item);

        List<ItemRestaurante> resultado = itemRestauranteRepository.findByNomeContainingIgnoreCase("feijoada");

        assertThat(resultado).isNotEmpty();
        assertThat(resultado.get(0).getNome()).isEqualTo("Feijoada");
    }

    @Test
    @DisplayName("Deve buscar itens com preço menor ou igual ao valor informado")
    void deveBuscarPorPrecoMaximo() {
        Restaurante restaurante = new Restaurante();
        restaurante.setNome("Cozinha Caseira");
        restaurante.setEmail("contato@caseira.com");
        restaurante.setSenha("abc");
        restaurante.setCnpj("33333333000133");
        restauranteRepository.save(restaurante);

        ItemRestaurante i1 = new ItemRestaurante();
        i1.setNome("Prato Executivo");
        i1.setDescricao("Arroz, feijão, carne e salada");
        i1.setPreco(20.0);
        i1.setRestaurante(restaurante);

        ItemRestaurante i2 = new ItemRestaurante();
        i2.setNome("Strogonoff");
        i2.setDescricao("Com batata palha");
        i2.setPreco(30.0);
        i2.setRestaurante(restaurante);

        itemRestauranteRepository.saveAll(List.of(i1, i2));

        List<ItemRestaurante> resultado = itemRestauranteRepository.findByPrecoLessThanEqual(25.0);

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getNome()).isEqualTo("Prato Executivo");
    }
}
