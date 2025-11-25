package br.restaurante.controller;

import br.restaurante.dto.LoginRequest;
import br.restaurante.model.Restaurante;
import br.restaurante.service.FileStorageService;
import br.restaurante.service.RestauranteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class RestauranteControllerTest {

    @Mock private RestauranteService restauranteService;
    @Mock private FileStorageService fileStorageService;

    @InjectMocks private RestauranteController controller;

    private Restaurante restaurante;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        restaurante = new Restaurante();
        restaurante.setId(1L);
        restaurante.setNome("Sabores Grill");
        restaurante.setEmail("grill@sabor.com");
        restaurante.setSenha("123456");
    }

    // =====================================================
    // 1️⃣ Login com sucesso
    // =====================================================
    @Test
    @DisplayName("Deve retornar 200 e restaurante ao logar com credenciais válidas")
    void deveLogarComSucesso() {
        LoginRequest login = new LoginRequest();
        login.setEmail("grill@sabor.com");
        login.setSenha("123456");

        when(restauranteService.loginRestaurante(login)).thenReturn(restaurante);

        ResponseEntity<?> resposta = controller.login(login);

        assertThat(resposta.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(resposta.getBody()).isEqualTo(restaurante);
    }

    // =====================================================
    // 2️⃣ Login falho
    // =====================================================
    @Test
    @DisplayName("Deve retornar 401 se login for inválido")
    void deveRetornar401SeLoginInvalido() {
        LoginRequest login = new LoginRequest();
        login.setEmail("invalido@sabor.com");
        login.setSenha("errada");

        when(restauranteService.loginRestaurante(login)).thenReturn(null);

        ResponseEntity<?> resposta = controller.login(login);

        assertThat(resposta.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(((Map<?, ?>) resposta.getBody()).get("message"))
                .isEqualTo("Email ou senha incorretos.");
    }

    // =====================================================
    // 3️⃣ Cadastro com sucesso
    // =====================================================
    @Test
    @DisplayName("Deve cadastrar restaurante com sucesso e retornar 201")
    void deveCadastrarRestauranteComSucesso() {
        when(restauranteService.cadastrarRestaurante(any(Restaurante.class)))
                .thenReturn(restaurante);

        ResponseEntity<?> resposta = controller.create(restaurante);

        assertThat(resposta.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(resposta.getBody()).isEqualTo(restaurante);
    }

    // =====================================================
    // 4️⃣ Cadastro com InputMismatchException
    // =====================================================
    @Test
    @DisplayName("Deve retornar 400 se dados inválidos forem enviados")
    void deveRetornar400SeDadosInvalidos() {
        when(restauranteService.cadastrarRestaurante(any(Restaurante.class)))
                .thenThrow(new InputMismatchException("Erro de validação"));

        ResponseEntity<?> resposta = controller.create(restaurante);

        assertThat(resposta.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(((Map<?, ?>) resposta.getBody()).get("message"))
                .isEqualTo("Erro de validação");
    }

    // =====================================================
    // 5️⃣ Upload de arquivo com sucesso (com tipo)
    // =====================================================
    @Test
    @DisplayName("Deve fazer upload com sucesso e retornar URL")
    void deveFazerUploadComSucesso() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file", "logo.png", "image/png", "conteudo".getBytes());
        when(fileStorageService.saveFile(any(), eq("logo"))).thenReturn("https://files/logo.png");

        ResponseEntity<?> resposta = controller.uploadByTipo("logo", file);

        assertThat(resposta.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(((Map<?, ?>) resposta.getBody()).get("url"))
                .isEqualTo("https://files/logo.png");
    }

    // =====================================================
    // 6️⃣ Upload com exceção
    // =====================================================
    @Test
    @DisplayName("Deve retornar 400 se upload falhar")
    void deveRetornar400SeUploadFalhar() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "erro.png", "image/png", "bad".getBytes());
        when(fileStorageService.saveFile(any(), eq("logo"))).thenThrow(new RuntimeException("Falha"));

        ResponseEntity<?> resposta = controller.uploadByTipo("logo", file);

        assertThat(resposta.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(((Map<?, ?>) resposta.getBody()).get("message"))
                .isEqualTo("Falha");
    }

    // =====================================================
    // 7️⃣ Buscar todos
    // =====================================================
    @Test
    @DisplayName("Deve retornar lista de restaurantes com 200")
    void deveBuscarTodos() {
        when(restauranteService.buscarTodos()).thenReturn(List.of(restaurante));

        ResponseEntity<List<Restaurante>> resposta = controller.readAll();

        assertThat(resposta.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(resposta.getBody()).hasSize(1);
    }

    // =====================================================
    // 8️⃣ Buscar por ID existente
    // =====================================================
    @Test
    @DisplayName("Deve retornar restaurante por ID com 200")
    void deveBuscarPorIdComSucesso() {
        when(restauranteService.buscarPorId(1L)).thenReturn(Optional.of(restaurante));

        ResponseEntity<Restaurante> resposta = controller.readById(1L);

        assertThat(resposta.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(resposta.getBody().getNome()).isEqualTo("Sabores Grill");
    }

    // =====================================================
    // 9️⃣ Buscar por ID inexistente
    // =====================================================
    @Test
    @DisplayName("Deve retornar 404 se restaurante não existir")
    void deveRetornar404SeRestauranteNaoExistir() {
        when(restauranteService.buscarPorId(1L)).thenReturn(Optional.empty());

        ResponseEntity<Restaurante> resposta = controller.readById(1L);

        assertThat(resposta.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    // =====================================================
    // 🔟 Atualizar com sucesso
    // =====================================================
    @Test
    @DisplayName("Deve atualizar restaurante existente com 200")
    void deveAtualizarComSucesso() {
        when(restauranteService.atualizarRestaurante(eq(1L), any(Restaurante.class)))
                .thenReturn(Optional.of(restaurante));

        ResponseEntity<Restaurante> resposta = controller.update(1L, restaurante);

        assertThat(resposta.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(restauranteService).atualizarRestaurante(eq(1L), any(Restaurante.class));
    }

    // =====================================================
    // 11️⃣ Atualizar inexistente
    // =====================================================
    @Test
    @DisplayName("Deve retornar 404 ao tentar atualizar restaurante inexistente")
    void deveRetornar404AoAtualizarInexistente() {
        when(restauranteService.atualizarRestaurante(eq(1L), any(Restaurante.class)))
                .thenReturn(Optional.empty());

        ResponseEntity<Restaurante> resposta = controller.update(1L, restaurante);

        assertThat(resposta.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    // =====================================================
    // 12️⃣ Deletar com sucesso
    // =====================================================
    @Test
    @DisplayName("Deve deletar restaurante existente com 204")
    void deveDeletarComSucesso() {
        when(restauranteService.deletarRestaurante(1L)).thenReturn(true);

        ResponseEntity<Void> resposta = controller.delete(1L);

        assertThat(resposta.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    // =====================================================
    // 13️⃣ Deletar inexistente
    // =====================================================
    @Test
    @DisplayName("Deve retornar 404 ao deletar restaurante inexistente")
    void deveRetornar404AoDeletarInexistente() {
        when(restauranteService.deletarRestaurante(1L)).thenReturn(false);

        ResponseEntity<Void> resposta = controller.delete(1L);

        assertThat(resposta.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
