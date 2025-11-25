package br.restaurante.service;

import br.restaurante.model.AvaliacaoPrato;
import br.restaurante.model.Cliente;
import br.restaurante.model.ItemRestaurante;
import br.restaurante.repository.AvaliacaoPratoRepository;
import br.restaurante.repository.ClienteRepository;
import br.restaurante.repository.ItemRestauranteRepository;
import br.restaurante.repository.PedidoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.InputMismatchException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AvaliacaoPratoServiceTest {

    @Mock
    private AvaliacaoPratoRepository avaliacaoPratoRepository;

    @Mock
    private ItemRestauranteRepository itemRestauranteRepository;

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private AvaliacaoPratoService avaliacaoPratoService;

    private AvaliacaoPrato avaliacao;
    private ItemRestaurante prato;
    private Cliente cliente;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        prato = new ItemRestaurante();
        prato.setId(1L);

        cliente = new Cliente();
        cliente.setId(1L);
        cliente.setEmail("teste@cliente.com");

        avaliacao = new AvaliacaoPrato();
        avaliacao.setNota(5.0);
        avaliacao.setPrato(prato);
    }

    @Test
    void deveCadastrarAvaliacaoComSucesso() {
        when(itemRestauranteRepository.findById(1L)).thenReturn(Optional.of(prato));
        when(clienteRepository.findByEmail("teste@cliente.com")).thenReturn(Optional.of(cliente));
        when(avaliacaoPratoRepository.save(any(AvaliacaoPrato.class))).thenAnswer(i -> {
            AvaliacaoPrato a = i.getArgument(0);
            a.setId(100L);
            a.setDataAvaliacao(LocalDateTime.now());
            return a;
        });

        AvaliacaoPrato resultado = avaliacaoPratoService.cadastrarAvaliacaoPrato(avaliacao, "teste@cliente.com");

        assertNotNull(resultado.getId());
        assertNotNull(resultado.getDataAvaliacao());
        assertEquals(cliente, resultado.getCliente());
        verify(avaliacaoPratoRepository, times(1)).save(any(AvaliacaoPrato.class));
    }

    @Test
    void deveLancarExcecaoQuandoNotaForNula() {
        avaliacao.setNota(null);
        InputMismatchException ex = assertThrows(InputMismatchException.class, () ->
                avaliacaoPratoService.cadastrarAvaliacaoPrato(avaliacao, "teste@cliente.com"));
        assertEquals("A nota da avaliação é obrigatória.", ex.getMessage());
    }

    @Test
    void deveLancarExcecaoQuandoNotaForInvalida() {
        avaliacao.setNota(10.0);
        InputMismatchException ex = assertThrows(InputMismatchException.class, () ->
                avaliacaoPratoService.cadastrarAvaliacaoPrato(avaliacao, "teste@cliente.com"));
        assertEquals("A nota deve ser um valor entre 1 e 5.", ex.getMessage());
    }

    @Test
    void deveLancarExcecaoQuandoPratoNaoExistir() {
        when(itemRestauranteRepository.findById(1L)).thenReturn(Optional.empty());
        InputMismatchException ex = assertThrows(InputMismatchException.class, () ->
                avaliacaoPratoService.cadastrarAvaliacaoPrato(avaliacao, "teste@cliente.com"));
        assertEquals("Prato não encontrado. Verifique o ID do prato.", ex.getMessage());
    }

    @Test
    void deveLancarExcecaoQuandoClienteNaoForEncontrado() {
        when(itemRestauranteRepository.findById(1L)).thenReturn(Optional.of(prato));
        when(clienteRepository.findByEmail("teste@cliente.com")).thenReturn(Optional.empty());
        InputMismatchException ex = assertThrows(InputMismatchException.class, () ->
                avaliacaoPratoService.cadastrarAvaliacaoPrato(avaliacao, "teste@cliente.com"));
        assertEquals("Cliente não encontrado.", ex.getMessage());
    }

    // ===== TESTES COMPLEMENTARES =====

    @Test
    void deveLancarExcecaoQuandoClienteSemEmail() {
        when(itemRestauranteRepository.findById(1L)).thenReturn(Optional.of(prato));
        cliente.setEmail(null);
        when(clienteRepository.findByEmail(null)).thenReturn(Optional.empty());

        InputMismatchException ex = assertThrows(InputMismatchException.class, () ->
                avaliacaoPratoService.cadastrarAvaliacaoPrato(avaliacao, null));

        assertEquals("Cliente não encontrado.", ex.getMessage());
    }

    @Test
    void devePermitirNotaLimiteMinimo() {
        avaliacao.setNota(1.0);
        when(itemRestauranteRepository.findById(1L)).thenReturn(Optional.of(prato));
        when(clienteRepository.findByEmail("teste@cliente.com")).thenReturn(Optional.of(cliente));
        when(avaliacaoPratoRepository.save(any(AvaliacaoPrato.class))).thenReturn(avaliacao);

        AvaliacaoPrato resultado = avaliacaoPratoService.cadastrarAvaliacaoPrato(avaliacao, "teste@cliente.com");

        assertNotNull(resultado);
        assertEquals(1.0, resultado.getNota());
    }

    @Test
    void devePermitirNotaLimiteMaximo() {
        avaliacao.setNota(5.0);
        when(itemRestauranteRepository.findById(1L)).thenReturn(Optional.of(prato));
        when(clienteRepository.findByEmail("teste@cliente.com")).thenReturn(Optional.of(cliente));
        when(avaliacaoPratoRepository.save(any(AvaliacaoPrato.class))).thenReturn(avaliacao);

        AvaliacaoPrato resultado = avaliacaoPratoService.cadastrarAvaliacaoPrato(avaliacao, "teste@cliente.com");

        assertNotNull(resultado);
        assertEquals(5.0, resultado.getNota());
    }

    @Test
    void deveRegistrarDataDaAvaliacaoAutomaticamente() {
        when(itemRestauranteRepository.findById(1L)).thenReturn(Optional.of(prato));
        when(clienteRepository.findByEmail("teste@cliente.com")).thenReturn(Optional.of(cliente));
        when(avaliacaoPratoRepository.save(any(AvaliacaoPrato.class))).thenAnswer(i -> {
            AvaliacaoPrato a = i.getArgument(0);
            a.setDataAvaliacao(LocalDateTime.now());
            return a;
        });

        AvaliacaoPrato resultado = avaliacaoPratoService.cadastrarAvaliacaoPrato(avaliacao, "teste@cliente.com");

        assertNotNull(resultado.getDataAvaliacao());
    }

    @Test
    void deveSalvarClienteEPratoCorretosNaAvaliacao() {
        when(itemRestauranteRepository.findById(1L)).thenReturn(Optional.of(prato));
        when(clienteRepository.findByEmail("teste@cliente.com")).thenReturn(Optional.of(cliente));
        when(avaliacaoPratoRepository.save(any(AvaliacaoPrato.class))).thenReturn(avaliacao);

        AvaliacaoPrato resultado = avaliacaoPratoService.cadastrarAvaliacaoPrato(avaliacao, "teste@cliente.com");

        assertEquals(cliente, resultado.getCliente());
        assertEquals(prato, resultado.getPrato());
        verify(avaliacaoPratoRepository, times(1)).save(any(AvaliacaoPrato.class));
    }
}
