package br.restaurante.service;

import br.restaurante.dto.LoginRequest;
import br.restaurante.model.Cliente;
import br.restaurante.model.Endereco;
import br.restaurante.repository.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para ClienteService
 * Valida as principais regras de negócio e autenticação.
 */
class ClienteServiceTest {

    @InjectMocks
    private ClienteService clienteService;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private ViaCepService viaCepService;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    private Cliente cliente;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNome("Persio Lima");
        cliente.setCpf("12345678901");
        cliente.setEmail("persio@sabore.com");
        cliente.setSenha("senha123");
        cliente.setCep("01001000");
    }

    // ✅ TESTE: cadastro bem-sucedido
    @Test
    void deveCadastrarClienteComSucesso() {
        Endereco enderecoMock = new Endereco("01001000", "Rua Teste", "Centro", "São Paulo", "SP");

        when(viaCepService.buscaEnderecoPorCep("01001000")).thenReturn(enderecoMock);
        when(passwordEncoder.encode("senha123")).thenReturn("hashSenha");
        when(clienteRepository.save(any(Cliente.class))).thenAnswer(i -> {
            Cliente c = i.getArgument(0);
            c.setId(100L);
            return c;
        });

        Cliente resultado = clienteService.cadastrarCliente(cliente);

        assertNotNull(resultado);
        assertEquals(100L, resultado.getId());
        assertEquals("hashSenha", resultado.getSenha());
        assertEquals("Rua Teste", resultado.getRua());
        verify(clienteRepository, times(1)).save(any(Cliente.class));
    }

    // ❌ TESTE: campos obrigatórios ausentes
    @Test
    void deveLancarExcecaoQuandoCamposObrigatoriosFaltarem() {
        cliente.setNome("");
        InputMismatchException ex = assertThrows(InputMismatchException.class, () ->
                clienteService.cadastrarCliente(cliente));
        assertEquals("Os campos Nome, CPF, Email e Senha são obrigatórios.", ex.getMessage());
    }

    // ❌ TESTE: CPF inválido
    @Test
    void deveLancarExcecaoQuandoCpfForInvalido() {
        cliente.setCpf("123");
        InputMismatchException ex = assertThrows(InputMismatchException.class, () ->
                clienteService.cadastrarCliente(cliente));
        assertEquals("O CPF deve conter 11 dígitos.", ex.getMessage());
    }

    // ❌ TESTE: e-mail inválido
    @Test
    void deveLancarExcecaoQuandoEmailForInvalido() {
        cliente.setEmail("email-invalido");
        InputMismatchException ex = assertThrows(InputMismatchException.class, () ->
                clienteService.cadastrarCliente(cliente));
        assertEquals("O formato do email é inválido.", ex.getMessage());
    }

    // ❌ TESTE: CPF duplicado
    @Test
    void deveLancarExcecaoQuandoCpfJaCadastrado() {
        when(clienteRepository.findByCpf("12345678901")).thenReturn(Optional.of(cliente));
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                clienteService.cadastrarCliente(cliente));
        assertEquals("CPF já cadastrado.", ex.getMessage());
    }

    // ❌ TESTE: e-mail duplicado
    @Test
    void deveLancarExcecaoQuandoEmailJaCadastrado() {
        when(clienteRepository.findByCpf("12345678901")).thenReturn(Optional.empty());
        when(clienteRepository.findByEmail("persio@sabore.com")).thenReturn(Optional.of(cliente));
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                clienteService.cadastrarCliente(cliente));
        assertEquals("Email já cadastrado.", ex.getMessage());
    }

    // ✅ TESTE: login bem-sucedido
    @Test
    void deveFazerLoginComSucesso() {
        when(clienteRepository.findByEmail("persio@sabore.com")).thenReturn(Optional.of(cliente));
        when(passwordEncoder.matches("senha123", "senha123")).thenReturn(true);

        LoginRequest login = new LoginRequest("persio@sabore.com", "senha123");
        Cliente resultado = clienteService.loginCliente(login);

        assertNotNull(resultado);
        assertEquals("persio@sabore.com", resultado.getEmail());
    }

    // ❌ TESTE: login com senha incorreta
    @Test
    void deveRetornarNullQuandoSenhaIncorreta() {
        when(clienteRepository.findByEmail("persio@sabore.com")).thenReturn(Optional.of(cliente));
        when(passwordEncoder.matches("errada", "senha123")).thenReturn(false);

        LoginRequest login = new LoginRequest("persio@sabore.com", "errada");
        Cliente resultado = clienteService.loginCliente(login);

        assertNull(resultado);
    }

    // ✅ TESTE: buscar todos os clientes
    @Test
    void deveBuscarTodosClientes() {
        when(clienteRepository.findAll()).thenReturn(List.of(cliente));
        List<Cliente> lista = clienteService.buscarTodos();
        assertEquals(1, lista.size());
        verify(clienteRepository, times(1)).findAll();
    }

    // ✅ TESTE: deletar cliente existente
    @Test
    void deveDeletarClienteComSucesso() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        boolean resultado = clienteService.deletarCliente(1L);
        assertTrue(resultado);
        verify(clienteRepository, times(1)).deleteById(1L);
    }

    // ❌ TESTE: deletar cliente inexistente
    @Test
    void deveRetornarFalseAoDeletarClienteInexistente() {
        when(clienteRepository.findById(2L)).thenReturn(Optional.empty());
        boolean resultado = clienteService.deletarCliente(2L);
        assertFalse(resultado);
    }

    // ✅ TESTE: atualizar cliente com sucesso
    @Test
    void deveAtualizarClienteComSucesso() {
        Cliente atualizado = new Cliente();
        atualizado.setNome("Novo Nome");
        atualizado.setEmail("novo@email.com");

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(clienteRepository.save(any(Cliente.class))).thenAnswer(i -> i.getArgument(0));

        Optional<Cliente> resultado = clienteService.atualizarCliente(1L, atualizado);

        assertTrue(resultado.isPresent());
        assertEquals("Novo Nome", resultado.get().getNome());
        assertEquals("novo@email.com", resultado.get().getEmail());
        verify(clienteRepository, times(1)).save(any(Cliente.class));
    }

    // ❌ TESTE: atualizar cliente inexistente
    @Test
    void deveRetornarEmptyAoAtualizarClienteInexistente() {
        when(clienteRepository.findById(99L)).thenReturn(Optional.empty());
        Optional<Cliente> resultado = clienteService.atualizarCliente(99L, cliente);
        assertTrue(resultado.isEmpty());
        verify(clienteRepository, never()).save(any());
    }
}
