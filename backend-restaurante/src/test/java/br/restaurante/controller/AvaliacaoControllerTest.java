package br.restaurante.controller;

import br.restaurante.model.Avaliacao;
import br.restaurante.service.AvaliacaoService;
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

@WebMvcTest(AvaliacaoController.class)
@AutoConfigureMockMvc(addFilters = false)
class AvaliacaoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AvaliacaoService avaliacaoService;

    @Test
    void deveListarTodasAsAvaliacoes() throws Exception {
        Avaliacao a1 = new Avaliacao();
        a1.setComentario("Comida excelente!");
        Avaliacao a2 = new Avaliacao();
        a2.setComentario("Muito bom!");

        when(avaliacaoService.buscarTodasAsAvaliacoes()).thenReturn(List.of(a1, a2));

        mockMvc.perform(get("/avaliacoes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].comentario").value("Comida excelente!"))
                .andExpect(jsonPath("$[1].comentario").value("Muito bom!"));
    }

    @Test
    void deveBuscarAvaliacoesPorRestaurante() throws Exception {
        Avaliacao a1 = new Avaliacao();
        a1.setComentario("Prato ótimo!");

        when(avaliacaoService.buscarAvaliacoesPorRestauranteId(1L))
                .thenReturn(List.of(a1));

        mockMvc.perform(get("/avaliacoes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].comentario").value("Prato ótimo!"));
    }

    @Test
    void deveCriarAvaliacaoComUsuarioAutenticado() throws Exception {
        Avaliacao avaliacao = new Avaliacao();
        avaliacao.setComentario("Excelente atendimento!");

        when(avaliacaoService.buscarClienteIdPorEmail("cliente@teste.com"))
                .thenReturn(Optional.of(1L));
        when(avaliacaoService.cadastrarAvaliacao(any(Avaliacao.class), eq(1L)))
                .thenReturn(avaliacao);

        // 🔹 Simula autenticação manualmente no contexto
        var user = new User("cliente@teste.com", "123", java.util.Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(user, "123", user.getAuthorities())
        );

        mockMvc.perform(post("/avaliacoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"comentario\":\"Excelente atendimento!\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.comentario").value("Excelente atendimento!"));

        // 🔹 Limpa o contexto de segurança após o teste
        SecurityContextHolder.clearContext();
    }

    @Test
    void deveNegarCriacaoDeAvaliacaoSemUsuario() throws Exception {
        mockMvc.perform(post("/avaliacoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"comentario\":\"Sem login\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message")
                        .value("Você precisa estar logado para fazer uma avaliação."));
    }
}
