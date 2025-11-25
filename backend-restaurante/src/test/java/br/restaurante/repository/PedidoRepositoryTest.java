package br.restaurante.repository;

import br.restaurante.model.Cliente;
import br.restaurante.model.Pedido;
import br.restaurante.model.Restaurante;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class PedidoRepositoryTest {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private RestauranteRepository restauranteRepository;

    @Test
    @DisplayName("Deve buscar pedidos de um cliente em ordem decrescente de criação")
    void deveBuscarPedidosPorClienteEmOrdemDecrescente() {
        // Cria restaurante associado aos pedidos
        Restaurante restaurante = new Restaurante();
        restaurante.setNome("Sabores da Casa");
        restaurante.setEmail("contato@sabores.com");
        restaurante.setSenha("123");
        restaurante.setCnpj("11111111000111");
        restauranteRepository.save(restaurante);

        // Cria cliente
        Cliente cliente = new Cliente();
        cliente.setNome("João da Silva");
        cliente.setCpf("12345678901");
        cliente.setEmail("joao@email.com");
        cliente.setSenha("senha123");
        cliente.setAceitaProtecaoDados(true);
        clienteRepository.save(cliente);

        // Cria pedidos
        Pedido pedido1 = new Pedido();
        pedido1.setCliente(cliente);
        pedido1.setRestaurante(restaurante);
        pedido1.setCriadoEm(LocalDateTime.now().minusDays(2)); // mais antigo
        pedido1.setStatus("FINALIZADO");

        Pedido pedido2 = new Pedido();
        pedido2.setCliente(cliente);
        pedido2.setRestaurante(restaurante);
        pedido2.setCriadoEm(LocalDateTime.now()); // mais recente
        pedido2.setStatus("EM ANDAMENTO");

        pedidoRepository.saveAll(List.of(pedido1, pedido2));

        List<Pedido> resultado = pedidoRepository.findByClienteOrderByCriadoEmDesc(cliente);

        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).getStatus()).isEqualTo("EM ANDAMENTO");
        assertThat(resultado.get(1).getStatus()).isEqualTo("FINALIZADO");
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando o cliente não tiver pedidos")
    void deveRetornarListaVaziaSeNaoHouverPedidos() {
        Restaurante restaurante = new Restaurante();
        restaurante.setNome("Casa Mineira");
        restaurante.setEmail("mineira@email.com");
        restaurante.setSenha("1234");
        restaurante.setCnpj("22222222000122");
        restauranteRepository.save(restaurante);

        Cliente cliente = new Cliente();
        cliente.setNome("Maria Oliveira");
        cliente.setCpf("98765432100");
        cliente.setEmail("maria@email.com");
        cliente.setSenha("123456");
        cliente.setAceitaProtecaoDados(false);
        clienteRepository.save(cliente);

        List<Pedido> resultado = pedidoRepository.findByClienteOrderByCriadoEmDesc(cliente);

        assertThat(resultado).isEmpty();
    }
}
