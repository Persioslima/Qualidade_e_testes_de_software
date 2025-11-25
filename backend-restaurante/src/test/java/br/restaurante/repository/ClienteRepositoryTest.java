package br.restaurante.repository;

import br.restaurante.model.Cliente;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ClienteRepositoryTest {

    @Autowired
    private ClienteRepository clienteRepository;

    @Test
    @DisplayName("Deve salvar e buscar cliente pelo CPF")
    void deveBuscarPorCpf() {
        Cliente cliente = new Cliente();
        cliente.setNome("João da Silva");
        cliente.setCpf("12345678901");
        cliente.setEmail("joao@email.com");
        cliente.setSenha("senha123");
        cliente.setAceitaProtecaoDados(true);

        clienteRepository.save(cliente);

        Optional<Cliente> resultado = clienteRepository.findByCpf("12345678901");

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getNome()).isEqualTo("João da Silva");
    }

    @Test
    @DisplayName("Deve salvar e buscar cliente pelo email")
    void deveBuscarPorEmail() {
        Cliente cliente = new Cliente();
        cliente.setNome("Maria Oliveira");
        cliente.setCpf("98765432100");
        cliente.setEmail("maria@email.com");
        cliente.setSenha("123456");
        cliente.setAceitaProtecaoDados(false);

        clienteRepository.save(cliente);

        Optional<Cliente> resultado = clienteRepository.findByEmail("maria@email.com");

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getCpf()).isEqualTo("98765432100");
    }

    @Test
    @DisplayName("Deve buscar clientes que aceitaram a proteção de dados")
    void deveBuscarPorAceiteProtecaoDados() {
        Cliente c1 = new Cliente();
        c1.setNome("Carlos");
        c1.setCpf("11111111111");
        c1.setEmail("carlos@email.com");
        c1.setSenha("abc");
        c1.setAceitaProtecaoDados(true);

        Cliente c2 = new Cliente();
        c2.setNome("Ana");
        c2.setCpf("22222222222");
        c2.setEmail("ana@email.com");
        c2.setSenha("def");
        c2.setAceitaProtecaoDados(false);

        Cliente c3 = new Cliente();
        c3.setNome("Paula");
        c3.setCpf("33333333333");
        c3.setEmail("paula@email.com");
        c3.setSenha("ghi");
        c3.setAceitaProtecaoDados(true);

        clienteRepository.saveAll(List.of(c1, c2, c3));

        List<Cliente> aceitou = clienteRepository.findByAceitaProtecaoDados(true);

        assertThat(aceitou).hasSize(2);
        assertThat(aceitou).extracting(Cliente::getNome)
                .containsExactlyInAnyOrder("Carlos", "Paula");
    }
}
