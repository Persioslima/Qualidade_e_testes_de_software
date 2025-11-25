package br.restaurante.service;

import br.restaurante.model.Avaliacao;
import br.restaurante.model.Cliente;
import br.restaurante.model.Restaurante;
import br.restaurante.repository.AvaliacaoRepository;
import br.restaurante.repository.ClienteRepository;
import br.restaurante.repository.RestauranteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Teste unitário para AvaliacaoService
 * Foco: validar regras de negócio e tratamento de exceções.
 */
class AvaliacaoServiceTest {

    @InjectMocks
    private AvaliacaoService avaliacaoService;

    @Mock
    private AvaliacaoRepository avaliacaoRepository;

    @Mock
    private RestauranteRepository restauranteRepository;

    @Mock
    private ClienteRepository clienteRepository;

    private Avaliacao avaliacao;
    private Restaurante restaurante;
    private Cliente cliente;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        restaurante = new Restaurante();
        restaurante.setId(1L);
        restaurante.setNome("Saborê Restaurante");

        cliente = new Cliente();
        cliente.setId(1L);
        cliente.setEmail("teste@cliente.com");

        avaliacao = new Avaliacao();
        avaliacao.setNota(5.0);
        avaliacao.setRestaurante(restaurante);
    }

    // ✅ TESTE: cadastro válido
    @Test
    void deveCadastrarAvaliacaoComSucesso() {
        when(restauranteRepository.findById(1L)).thenReturn(Optional.of(restaurante));
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(avaliacaoRepository.save(any(Avaliacao.class))).thenAnswer(i -> {
            Avaliacao a = i.getArgument(0);
            a.setId(100L);
            a.setDataAvaliacao(LocalDateTime.now());
            return a;
        });

        Avaliacao resultado = avaliacaoService.cadastrarAvaliacao(avaliacao, 1L);

        assertNotNull(resultado);
        assertEquals(100L, resultado.getId());
        assertEquals(5.0, resultado.getNota());
        assertEquals(restaurante, resultado.getRestaurante());
        assertEquals(cliente, resultado.getCliente());
        assertNotNull(resultado.getDataAvaliacao());
        verify(avaliacaoRepository, times(1)).save(any(Avaliacao.class));
    }

    // ❌ TESTE: nota nula
    @Test
    void deveLancarExcecaoQuandoNotaForNula() {
        avaliacao.setNota(null);
        InputMismatchException ex = assertThrows(InputMismatchException.class, () ->
                avaliacaoService.cadastrarAvaliacao(avaliacao, 1L));
        assertEquals("A nota da avaliação é obrigatória.", ex.getMessage());
    }

    // ❌ TESTE: nota fora do intervalo
    @Test
    void deveLancarExcecaoQuandoNotaForInvalida() {
        avaliacao.setNota(10.0);
        InputMismatchException ex = assertThrows(InputMismatchException.class, () ->
                avaliacaoService.cadastrarAvaliacao(avaliacao, 1L));
        assertEquals("A nota deve ser um valor entre 1 e 5.", ex.getMessage());
    }

    // ❌ TESTE: restaurante não informado
    @Test
    void deveLancarExcecaoQuandoRestauranteNaoInformado() {
        avaliacao.setRestaurante(null);
        InputMismatchException ex = assertThrows(InputMismatchException.class, () ->
                avaliacaoService.cadastrarAvaliacao(avaliacao, 1L));
        assertEquals("É necessário associar a avaliação a um restaurante.", ex.getMessage());
    }

    // ❌ TESTE: restaurante não encontrado
    @Test
    void deveLancarExcecaoQuandoRestauranteNaoEncontrado() {
        when(restauranteRepository.findById(1L)).thenReturn(Optional.empty());
        InputMismatchException ex = assertThrows(InputMismatchException.class, () ->
                avaliacaoService.cadastrarAvaliacao(avaliacao, 1L));
        assertEquals("Restaurante não encontrado. Verifique o ID do restaurante.", ex.getMessage());
    }

    // ❌ TESTE: cliente não encontrado
    @Test
    void deveLancarExcecaoQuandoClienteNaoEncontrado() {
        when(restauranteRepository.findById(1L)).thenReturn(Optional.of(restaurante));
        when(clienteRepository.findById(1L)).thenReturn(Optional.empty());
        InputMismatchException ex = assertThrows(InputMismatchException.class, () ->
                avaliacaoService.cadastrarAvaliacao(avaliacao, 1L));
        assertEquals("Cliente não encontrado. Você precisa estar logado.", ex.getMessage());
    }

    // ✅ TESTE: buscar por restaurante
    @Test
    void deveBuscarAvaliacoesPorRestaurante() {
        when(restauranteRepository.findById(1L)).thenReturn(Optional.of(restaurante));
        when(avaliacaoRepository.findByRestaurante(restaurante)).thenReturn(List.of(avaliacao));

        List<Avaliacao> resultado = avaliacaoService.buscarAvaliacoesPorRestauranteId(1L);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(5.0, resultado.get(0).getNota());
    }

    // ❌ TESTE: restaurante não encontrado na busca
    @Test
    void deveLancarExcecaoAoBuscarAvaliacoesDeRestauranteInexistente() {
        when(restauranteRepository.findById(99L)).thenReturn(Optional.empty());
        InputMismatchException ex = assertThrows(InputMismatchException.class, () ->
                avaliacaoService.buscarAvaliacoesPorRestauranteId(99L));
        assertEquals("Restaurante não encontrado.", ex.getMessage());
    }

    // ✅ TESTE: buscar cliente por e-mail
    @Test
    void deveBuscarClientePorEmail() {
        when(clienteRepository.findByEmail("teste@cliente.com")).thenReturn(Optional.of(cliente));
        Optional<Long> id = avaliacaoService.buscarClienteIdPorEmail("teste@cliente.com");
        assertTrue(id.isPresent());
        assertEquals(1L, id.get());
    }

    // ✅ TESTE: buscar todas as avaliações
    @Test
    void deveBuscarTodasAsAvaliacoes() {
        when(avaliacaoRepository.findAll()).thenReturn(List.of(avaliacao));
        List<Avaliacao> lista = avaliacaoService.buscarTodasAsAvaliacoes();
        assertEquals(1, lista.size());
        verify(avaliacaoRepository, times(1)).findAll();
    }

    // 🔹 TESTES COMPLEMENTARES (para aumentar cobertura)

    @Test
    void deveAceitarNotaLimiteMinima() {
        avaliacao.setNota(1.0);
        when(restauranteRepository.findById(1L)).thenReturn(Optional.of(restaurante));
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(avaliacaoRepository.save(any(Avaliacao.class))).thenReturn(avaliacao);

        Avaliacao resultado = avaliacaoService.cadastrarAvaliacao(avaliacao, 1L);

        assertEquals(1.0, resultado.getNota());
    }

    @Test
    void deveAceitarNotaLimiteMaxima() {
        avaliacao.setNota(5.0);
        when(restauranteRepository.findById(1L)).thenReturn(Optional.of(restaurante));
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(avaliacaoRepository.save(any(Avaliacao.class))).thenReturn(avaliacao);

        Avaliacao resultado = avaliacaoService.cadastrarAvaliacao(avaliacao, 1L);

        assertEquals(5.0, resultado.getNota());
    }

    @Test
    void deveRegistrarDataDaAvaliacaoAutomaticamente() {
        when(restauranteRepository.findById(1L)).thenReturn(Optional.of(restaurante));
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(avaliacaoRepository.save(any(Avaliacao.class))).thenAnswer(i -> {
            Avaliacao a = i.getArgument(0);
            a.setDataAvaliacao(LocalDateTime.now());
            return a;
        });

        Avaliacao resultado = avaliacaoService.cadastrarAvaliacao(avaliacao, 1L);

        assertNotNull(resultado.getDataAvaliacao());
    }

    @Test
    void deveRetornarClienteVazioQuandoEmailNaoEncontrado() {
        when(clienteRepository.findByEmail("naoexiste@teste.com")).thenReturn(Optional.empty());

        Optional<Long> id = avaliacaoService.buscarClienteIdPorEmail("naoexiste@teste.com");

        assertTrue(id.isEmpty());
    }
}
