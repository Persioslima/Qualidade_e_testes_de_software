package br.restaurante.service;

import br.restaurante.dto.ItemPedidoRequest;
import br.restaurante.dto.PedidoRequest;
import br.restaurante.model.*;
import br.restaurante.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class PedidoServiceTest {

    private PedidoRepository pedidoRepo;
    private ClienteRepository clienteRepo;
    private RestauranteRepository restRepo;
    private ItemRestauranteRepository itemRepo;
    private PedidoService service;

    @BeforeEach
    void setUp() {
        pedidoRepo = mock(PedidoRepository.class);
        clienteRepo = mock(ClienteRepository.class);
        restRepo = mock(RestauranteRepository.class);
        itemRepo = mock(ItemRestauranteRepository.class);

        service = new PedidoService();
        inject(service, "pedidoRepository", pedidoRepo);
        inject(service, "clienteRepository", clienteRepo);
        inject(service, "restauranteRepository", restRepo);
        inject(service, "itemRestauranteRepository", itemRepo);
    }

    // ==================== CRIAR PEDIDO ====================

    @Test
    @DisplayName("Deve lançar erro quando restauranteId é nulo")
    void deveLancarQuandoRestauranteIdNulo() {
        PedidoRequest req = new PedidoRequest();
        req.setRestauranteId(null);
        req.setItens(List.of(new ItemPedidoRequest()));

        assertThatThrownBy(() -> service.criarPedidoParaCliente("x@x.com", req))
                .isInstanceOf(InputMismatchException.class)
                .hasMessageContaining("informar o restaurante");
    }

    @Test
    @DisplayName("Deve lançar quando itens estão vazios")
    void itensVazios() {
        PedidoRequest req = new PedidoRequest();
        req.setRestauranteId(1L);
        req.setItens(List.of());

        assertThatThrownBy(() -> service.criarPedidoParaCliente("c@e.com", req))
                .isInstanceOf(InputMismatchException.class)
                .hasMessageContaining("pelo menos um item");
    }

    @Test
    @DisplayName("Deve lançar quando cliente não existe")
    void clienteNaoExiste() {
        PedidoRequest req = new PedidoRequest();
        req.setRestauranteId(1L);
        req.setItens(List.of(new ItemPedidoRequest()));

        when(clienteRepo.findByEmail("c@e.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.criarPedidoParaCliente("c@e.com", req))
                .isInstanceOf(InputMismatchException.class)
                .hasMessageContaining("Cliente não encontrado");
    }

    @Test
    @DisplayName("Deve lançar quando restaurante não existe")
    void restauranteNaoExiste() {
        var cliente = new Cliente(); cliente.setEmail("c@e.com");
        when(clienteRepo.findByEmail("c@e.com")).thenReturn(Optional.of(cliente));
        when(restRepo.findById(1L)).thenReturn(Optional.empty());

        PedidoRequest req = new PedidoRequest();
        req.setRestauranteId(1L);
        req.setItens(List.of(new ItemPedidoRequest()));

        assertThatThrownBy(() -> service.criarPedidoParaCliente("c@e.com", req))
                .isInstanceOf(InputMismatchException.class)
                .hasMessageContaining("Restaurante não encontrado");
    }

    @Test
    @DisplayName("Deve lançar quando item não existe")
    void itemNaoExiste() {
        var cliente = new Cliente(); cliente.setEmail("c@e.com");
        var rest = new Restaurante(); rest.setId(1L);
        when(clienteRepo.findByEmail("c@e.com")).thenReturn(Optional.of(cliente));
        when(restRepo.findById(1L)).thenReturn(Optional.of(rest));
        when(itemRepo.findById(anyLong())).thenReturn(Optional.empty());

        ItemPedidoRequest ip = new ItemPedidoRequest();
        ip.setItemRestauranteId(99L);
        ip.setQuantidade(1);

        PedidoRequest req = new PedidoRequest();
        req.setRestauranteId(1L);
        req.setItens(List.of(ip));

        assertThatThrownBy(() -> service.criarPedidoParaCliente("c@e.com", req))
                .isInstanceOf(InputMismatchException.class)
                .hasMessageContaining("Item do cardápio não encontrado");
    }

    @Test
    @DisplayName("Deve lançar quando item pertence a outro restaurante")
    void itemDeOutroRestaurante() {
        var cliente = new Cliente(); cliente.setEmail("c@e.com");
        when(clienteRepo.findByEmail("c@e.com")).thenReturn(Optional.of(cliente));

        var rest = new Restaurante(); rest.setId(10L);
        when(restRepo.findById(10L)).thenReturn(Optional.of(rest));

        var outroRest = new Restaurante(); outroRest.setId(999L);
        var item = new ItemRestaurante(); item.setId(5L); item.setRestaurante(outroRest);
        when(itemRepo.findById(5L)).thenReturn(Optional.of(item));

        ItemPedidoRequest ip = new ItemPedidoRequest();
        ip.setItemRestauranteId(5L);
        ip.setQuantidade(1);

        PedidoRequest req = new PedidoRequest();
        req.setRestauranteId(10L);
        req.setItens(List.of(ip));

        assertThatThrownBy(() -> service.criarPedidoParaCliente("c@e.com", req))
                .isInstanceOf(InputMismatchException.class)
                .hasMessageContaining("não pertence ao restaurante");
    }

    @Test
    @DisplayName("Deve criar pedido com sucesso quando dados são válidos")
    void criarPedidoValido() {
        var cliente = new Cliente(); cliente.setEmail("c@e.com");
        when(clienteRepo.findByEmail("c@e.com")).thenReturn(Optional.of(cliente));

        var rest = new Restaurante(); rest.setId(10L);
        when(restRepo.findById(10L)).thenReturn(Optional.of(rest));

        var item = new ItemRestaurante(); item.setId(5L); item.setRestaurante(rest);
        when(itemRepo.findById(5L)).thenReturn(Optional.of(item));

        ItemPedidoRequest ip = new ItemPedidoRequest();
        ip.setItemRestauranteId(5L);
        ip.setQuantidade(1);

        PedidoRequest req = new PedidoRequest();
        req.setRestauranteId(10L);
        req.setItens(List.of(ip));

        Pedido pedidoSalvo = new Pedido();
        when(pedidoRepo.save(any())).thenReturn(pedidoSalvo);

        Pedido resultado = service.criarPedidoParaCliente("c@e.com", req);

        assertThat(resultado).isNotNull();
        verify(pedidoRepo, times(1)).save(any(Pedido.class));
    }

    // ==================== LISTAR PEDIDOS ====================

    @Test
    @DisplayName("Deve listar pedidos de um cliente existente")
    void listarPedidosCliente() {
        var cliente = new Cliente(); cliente.setEmail("c@e.com");
        when(clienteRepo.findByEmail("c@e.com")).thenReturn(Optional.of(cliente));

        var pedido = new Pedido();
        when(pedidoRepo.findByClienteOrderByCriadoEmDesc(cliente))
                .thenReturn(List.of(pedido));

        List<Pedido> pedidos = service.listarPedidosDoCliente("c@e.com");

        assertThat(pedidos).hasSize(1);
        verify(pedidoRepo).findByClienteOrderByCriadoEmDesc(cliente);
    }

    @Test
    @DisplayName("Deve lançar quando cliente não existe ao listar pedidos")
    void listarPedidosClienteNaoExiste() {
        when(clienteRepo.findByEmail("x@x.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.listarPedidosDoCliente("x@x.com"))
                .isInstanceOf(InputMismatchException.class)
                .hasMessageContaining("Cliente não encontrado");
    }

    // ==================== ATUALIZAR STATUS ====================

    @Test
    @DisplayName("Deve atualizar status quando cliente é o dono do pedido")
    void atualizarStatusCorreto() {
        var cliente = new Cliente(); cliente.setEmail("a@a.com");
        var pedido = new Pedido();
        pedido.setCliente(cliente);

        when(pedidoRepo.findById(1L)).thenReturn(Optional.of(pedido));
        when(pedidoRepo.save(any())).thenReturn(pedido);

        Pedido atualizado = service.atualizarStatus(1L, "ENTREGUE", "a@a.com");

        assertThat(atualizado).isNotNull();
        verify(pedidoRepo).save(pedido);
    }

    @Test
    @DisplayName("Deve lançar se cliente não for dono do pedido")
    void atualizarStatusClienteErrado() {
        var cliente = new Cliente(); cliente.setEmail("certo@a.com");
        var pedido = new Pedido();
        pedido.setCliente(cliente);
        when(pedidoRepo.findById(1L)).thenReturn(Optional.of(pedido));

        assertThatThrownBy(() -> service.atualizarStatus(1L, "EM_ANDAMENTO", "errado@a.com"))
                .isInstanceOf(InputMismatchException.class)
                .hasMessageContaining("não pertence ao cliente");
    }

    // utilitário de injeção via reflexão
    private static void inject(Object target, String field, Object value) {
        try {
            var f = target.getClass().getDeclaredField(field);
            f.setAccessible(true);
            f.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
