package br.restaurante.service;

import br.restaurante.dto.LoginRequest;
import br.restaurante.model.Restaurante;
import br.restaurante.repository.RestauranteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class RestauranteServiceTest {

    private RestauranteRepository repo;
    private ViaCepService viaCep;
    private BCryptPasswordEncoder encoder;
    private RestauranteService service;

    @BeforeEach
    void setUp() {
        repo = mock(RestauranteRepository.class);
        viaCep = mock(ViaCepService.class);
        encoder = mock(BCryptPasswordEncoder.class);
        service = new RestauranteService();
        inject(service, "restauranteRepository", repo);
        inject(service, "viaCepService", viaCep);
        inject(service, "passwordEncoder", encoder);
    }

    // ======================================================
    // LOGIN
    // ======================================================

    @Test
    @DisplayName("Login deve falhar quando email inexistente")
    void loginFalhaEmailInexistente() {
        when(repo.findByEmail("x@y.com")).thenReturn(Optional.empty());

        var resp = service.loginRestaurante(new LoginRequest("x@y.com", "123"));
        assertThat(resp).isNull();
    }

    @Test
    @DisplayName("Login deve falhar quando senha não confere")
    void loginFalhaSenhaErrada() {
        var r = new Restaurante();
        r.setEmail("x@y.com");
        r.setSenha("$2a$10$hashfake");
        when(repo.findByEmail("x@y.com")).thenReturn(Optional.of(r));
        when(encoder.matches("errada", "$2a$10$hashfake")).thenReturn(false);

        var resp = service.loginRestaurante(new LoginRequest("x@y.com", "errada"));
        assertThat(resp).isNull();
    }

    @Test
    @DisplayName("Login deve ser bem-sucedido com credenciais válidas")
    void loginSucesso() {
        var r = new Restaurante();
        r.setEmail("x@y.com");
        r.setSenha("hash123");
        when(repo.findByEmail("x@y.com")).thenReturn(Optional.of(r));
        when(encoder.matches("123456", "hash123")).thenReturn(true);

        var resp = service.loginRestaurante(new LoginRequest("x@y.com", "123456"));
        assertThat(resp).isNotNull();
        assertThat(resp.getEmail()).isEqualTo("x@y.com");
    }

    // ======================================================
    // CADASTRO
    // ======================================================

    @Test
    @DisplayName("Deve cadastrar restaurante com sucesso")
    void cadastrarRestauranteComSucesso() {
        var restaurante = new Restaurante();
        restaurante.setNome("Saborê");
        restaurante.setCnpj("12345678000199");
        restaurante.setEmail("contato@sabore.com");
        restaurante.setSenha("123456");

        when(encoder.encode("123456")).thenReturn("hash123");
        when(repo.save(any(Restaurante.class))).thenAnswer(i -> {
            Restaurante r = i.getArgument(0);
            r.setId(1L);
            return r;
        });

        Restaurante salvo = service.cadastrarRestaurante(restaurante);

        assertThat(salvo.getId()).isEqualTo(1L);
        assertThat(salvo.getSenha()).isEqualTo("hash123");
        verify(repo).save(any(Restaurante.class));
    }

    @Test
    @DisplayName("Deve lançar erro ao cadastrar restaurante com campos obrigatórios ausentes")
    void cadastrarRestauranteCamposInvalidos() {
        var restaurante = new Restaurante(); // sem nome, email etc.
        assertThatThrownBy(() -> service.cadastrarRestaurante(restaurante))
                .isInstanceOf(InputMismatchException.class)
                .hasMessageContaining("Os campos Nome, CNPJ, Email e Senha são obrigatórios");
    }

    // ======================================================
    // ATUALIZAÇÃO
    // ======================================================

    @Test
    @DisplayName("Atualizar deve retornar Optional.empty quando id não existe")
    void atualizarRestauranteInexistente() {
        when(repo.findById(999L)).thenReturn(Optional.empty());

        var atualizado = service.atualizarRestaurante(999L, new Restaurante());
        assertThat(atualizado).isEmpty();
        verify(repo, never()).save(any());
    }

    @Test
    @DisplayName("Deve atualizar restaurante existente sem alterar senha")
    void atualizarRestauranteExistente() {
        var existente = new Restaurante();
        existente.setId(1L);
        existente.setNome("Antigo");
        existente.setSenha("hashAntigo");

        var atualizado = new Restaurante();
        atualizado.setNome("Novo Nome");
        atualizado.setSenha("novaSenha"); // não deve sobrescrever

        when(repo.findById(1L)).thenReturn(Optional.of(existente));
        when(repo.save(any(Restaurante.class))).thenAnswer(i -> i.getArgument(0));

        var resultado = service.atualizarRestaurante(1L, atualizado);

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getNome()).isEqualTo("Novo Nome");
        assertThat(resultado.get().getSenha()).isEqualTo("hashAntigo");
        verify(repo).save(any(Restaurante.class));
    }

    // ======================================================
    // CONSULTAS
    // ======================================================

    @Test
    @DisplayName("Deve retornar lista de restaurantes no buscarTodos")
    void deveBuscarTodos() {
        when(repo.findAll()).thenReturn(List.of(new Restaurante(), new Restaurante()));
        var lista = service.buscarTodos();
        assertThat(lista).hasSize(2);
        verify(repo).findAll();
    }

    @Test
    @DisplayName("Deve retornar restaurante existente por ID")
    void deveBuscarPorIdExistente() {
        var r = new Restaurante();
        r.setId(10L);
        when(repo.findById(10L)).thenReturn(Optional.of(r));

        var resultado = service.buscarPorId(10L);
        assertThat(resultado).isPresent();
        assertThat(resultado.get().getId()).isEqualTo(10L);
    }

    @Test
    @DisplayName("Deve retornar Optional.empty quando restaurante não existe no buscarPorId")
    void deveBuscarPorIdInexistente() {
        when(repo.findById(99L)).thenReturn(Optional.empty());
        var resultado = service.buscarPorId(99L);
        assertThat(resultado).isEmpty();
    }

    // ======================================================
    // DELETE
    // ======================================================

    @Test
    @DisplayName("Deve deletar restaurante existente")
    void deletarRestauranteExistente() {
        var r = new Restaurante();
        r.setId(1L);
        when(repo.findById(1L)).thenReturn(Optional.of(r));

        boolean result = service.deletarRestaurante(1L);

        assertThat(result).isTrue();
        verify(repo).deleteById(1L);
    }

    @Test
    @DisplayName("Deve retornar falso ao deletar restaurante inexistente")
    void deletarRestauranteInexistente() {
        when(repo.findById(2L)).thenReturn(Optional.empty());

        boolean result = service.deletarRestaurante(2L);

        assertThat(result).isFalse();
        verify(repo, never()).deleteById(anyLong());
    }

    // ======================================================
    // HELPER
    // ======================================================

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
