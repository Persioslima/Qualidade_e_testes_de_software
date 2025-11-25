package br.restaurante.repository;

import br.restaurante.model.Restaurante;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class RestauranteRepositoryTest {

    @Autowired
    private RestauranteRepository restauranteRepository;

    @Test
    @DisplayName("Deve salvar e buscar restaurante por CNPJ")
    void deveBuscarPorCnpj() {
        Restaurante restaurante = new Restaurante();
        restaurante.setNome("Churrascaria Gaúcha");
        restaurante.setEmail("contato@gaucha.com");
        restaurante.setSenha("123");
        restaurante.setCnpj("11111111000111");
        restaurante.setCidade("Curitiba");
        restaurante.setEstado("PR");
        restauranteRepository.save(restaurante);

        Optional<Restaurante> encontrado = restauranteRepository.findByCnpj("11111111000111");

        assertThat(encontrado).isPresent();
        assertThat(encontrado.get().getNome()).isEqualTo("Churrascaria Gaúcha");
    }

    @Test
    @DisplayName("Deve buscar restaurantes por cidade e estado")
    void deveBuscarPorCidadeEEstado() {
        Restaurante r1 = new Restaurante();
        r1.setNome("Sabores do Sul");
        r1.setEmail("sul@email.com");
        r1.setSenha("abc");
        r1.setCnpj("22222222000122");
        r1.setCidade("Curitiba");
        r1.setEstado("PR");

        Restaurante r2 = new Restaurante();
        r2.setNome("Temperos do Norte");
        r2.setEmail("norte@email.com");
        r2.setSenha("xyz");
        r2.setCnpj("33333333000133");
        r2.setCidade("Manaus");
        r2.setEstado("AM");

        restauranteRepository.saveAll(List.of(r1, r2));

        List<Restaurante> resultado = restauranteRepository.findByCidadeAndEstado("Curitiba", "PR");

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getNome()).isEqualTo("Sabores do Sul");
    }

    @Test
    @DisplayName("Deve buscar restaurante por nome ignorando maiúsculas e minúsculas")
    void deveBuscarPorNomeIgnoreCase() {
        Restaurante r1 = new Restaurante();
        r1.setNome("Pizzaria Napoli");
        r1.setEmail("napoli@email.com");
        r1.setSenha("xyz");
        r1.setCnpj("44444444000144");
        r1.setCidade("São Paulo");
        r1.setEstado("SP");
        restauranteRepository.save(r1);

        List<Restaurante> resultado = restauranteRepository.findByNomeContainingIgnoreCase("napoli");

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getNome()).isEqualTo("Pizzaria Napoli");
    }

    @Test
    @DisplayName("Deve buscar restaurante por email")
    void deveBuscarPorEmail() {
        Restaurante restaurante = new Restaurante();
        restaurante.setNome("Restaurante Central");
        restaurante.setEmail("central@email.com");
        restaurante.setSenha("abc123");
        restaurante.setCnpj("55555555000155");
        restaurante.setCidade("Londrina");
        restaurante.setEstado("PR");
        restauranteRepository.save(restaurante);

        Optional<Restaurante> encontrado = restauranteRepository.findByEmail("central@email.com");

        assertThat(encontrado).isPresent();
        assertThat(encontrado.get().getCidade()).isEqualTo("Londrina");
    }
}
