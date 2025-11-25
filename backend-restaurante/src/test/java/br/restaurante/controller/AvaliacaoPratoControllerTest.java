package br.restaurante.controller;

import br.restaurante.model.AvaliacaoPrato;
import br.restaurante.model.ItemRestaurante;
import br.restaurante.service.AvaliacaoPratoService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * ✅ Testes da camada Controller (AvaliacaoPratoController)
 * Utiliza MockMvc com segurança desativada e contexto de autenticação simulado.
 */
@WebMvcTest(AvaliacaoPratoController.class)
@AutoConfigureMockMvc(addFilters = false)
class AvaliacaoPratoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AvaliacaoPratoService avaliacaoPratoService;

    @BeforeEach
    void setupSecurityContext() {
        // 🔹 Simula autenticação válida antes de cada teste
        var user = new User("cliente@teste.com", "123", java.util.Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(user, "123", user.getAuthorities())
        );
    }

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void deveListarTodasAsAvaliacoesDePratos() throws Exception {
        AvaliacaoPrato a1 = new AvaliacaoPrato();
        a1.setComentario("Prato excelente!");
        AvaliacaoPrato a2 = new AvaliacaoPrato();
        a2.setComentario("Muito saboroso!");

        when(avaliacaoPratoService.buscarTodasAvaliacoes()).thenReturn(List.of(a1, a2));

        mockMvc.perform(get("/avaliacoes-prato"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].comentario").value("Prato excelente!"))
                .andExpect(jsonPath("$[1].comentario").value("Muito saboroso!"));
    }

    @Test
    void deveListarAvaliacoesPorItem() throws Exception {
        AvaliacaoPrato avaliacao = new AvaliacaoPrato();
        avaliacao.setComentario("Delicioso!");
        when(avaliacaoPratoService.buscarAvaliacoesPorPratoId(10L))
                .thenReturn(List.of(avaliacao));

        mockMvc.perform(get("/avaliacoes-prato/item/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].comentario").value("Delicioso!"));
    }

    @Test
    void deveCriarAvaliacaoComUsuarioAutenticadoEPedidoValido() throws Exception {
        AvaliacaoPrato avaliacao = new AvaliacaoPrato();
        avaliacao.setComentario("Excelente!");

        ItemRestaurante prato = new ItemRestaurante();
        prato.setId(5L);
        avaliacao.setPrato(prato);

        when(avaliacaoPratoService.clienteConcluiuPedidoComItem("cliente@teste.com", 5L))
                .thenReturn(true);
        when(avaliacaoPratoService.cadastrarAvaliacaoPrato(any(AvaliacaoPrato.class), eq("cliente@teste.com")))
                .thenReturn(avaliacao);

        mockMvc.perform(post("/avaliacoes-prato")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"comentario\":\"Excelente!\",\"prato\":{\"id\":5}}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.comentario").value("Excelente!"));
    }

    @Test
    void deveNegarCriacaoDeAvaliacaoSemLogin() throws Exception {
        // 🔹 Remove autenticação simulada para testar 401
        SecurityContextHolder.clearContext();

        mockMvc.perform(post("/avaliacoes-prato")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"comentario\":\"Teste sem login\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("É necessário estar logado."));
    }

    @Test
    void deveNegarAvaliacaoDeClienteSemPedidoConcluido() throws Exception {
        AvaliacaoPrato avaliacao = new AvaliacaoPrato();
        avaliacao.setComentario("Boa comida!");

        ItemRestaurante prato = new ItemRestaurante();
        prato.setId(9L);
        avaliacao.setPrato(prato);

        when(avaliacaoPratoService.clienteConcluiuPedidoComItem("cliente@teste.com", 9L))
                .thenReturn(false);

        mockMvc.perform(post("/avaliacoes-prato")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"comentario\":\"Boa comida!\",\"prato\":{\"id\":9}}"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value("Apenas clientes com pedidos concluídos contendo este item podem avaliar."));
    }
}
