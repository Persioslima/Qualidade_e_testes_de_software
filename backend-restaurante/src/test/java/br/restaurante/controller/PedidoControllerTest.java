package br.restaurante.controller;

import br.restaurante.dto.PedidoRequest;
import br.restaurante.model.Pedido;
import br.restaurante.service.PedidoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.InputMismatchException;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * ✅ Testes isolados do PedidoController
 * Usa WebMvcTest para não subir o contexto completo.
 */
@WebMvcTest(controllers = PedidoController.class)
@AutoConfigureMockMvc(addFilters = false) // Desativa os filtros de segurança
public class PedidoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PedidoService pedidoService;

    @Autowired
    private ObjectMapper objectMapper;

    private Pedido mockPedido;

    @BeforeEach
    void setup() throws Exception {
        mockPedido = new Pedido();
        var idField = Pedido.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(mockPedido, 1L);
        mockPedido.setStatus("NOVO");
    }

    @Test
    @WithMockUser(username = "cliente@teste.com")
    void deveCriarPedidoComSucesso() throws Exception {
        PedidoRequest request = new PedidoRequest();

        when(pedidoService.criarPedidoParaCliente(eq("cliente@teste.com"), any(PedidoRequest.class)))
                .thenReturn(mockPedido);

        mockMvc.perform(post("/pedidos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("NOVO"));
    }

    @Test
    @WithMockUser(username = "cliente@teste.com")
    void deveListarPedidosDoCliente() throws Exception {
        Pedido p1 = new Pedido();
        Pedido p2 = new Pedido();

        var idField = Pedido.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(p1, 10L);
        idField.set(p2, 20L);

        when(pedidoService.listarPedidosDoCliente("cliente@teste.com"))
                .thenReturn(List.of(p1, p2));

        mockMvc.perform(get("/pedidos"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "cliente@teste.com")
    void deveAtualizarStatusComSucesso() throws Exception {
        Pedido atualizado = new Pedido();
        var idField = Pedido.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(atualizado, 1L);
        atualizado.setStatus("CONCLUIDO");

        when(pedidoService.atualizarStatus(eq(1L), eq("CONCLUIDO"), eq("cliente@teste.com")))
                .thenReturn(atualizado);

        mockMvc.perform(put("/pedidos/1/status?status=CONCLUIDO"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CONCLUIDO"));
    }

    @Test
    void deveRetornarErro401AoCriarSemAutenticacao() throws Exception {
        PedidoRequest request = new PedidoRequest();
        mockMvc.perform(post("/pedidos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "cliente@teste.com")
    void deveTratarErroDeInputMismatch() throws Exception {
        when(pedidoService.criarPedidoParaCliente(eq("cliente@teste.com"), any(PedidoRequest.class)))
                .thenThrow(new InputMismatchException("Dados inválidos"));

        PedidoRequest request = new PedidoRequest();

        mockMvc.perform(post("/pedidos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Dados inválidos"));
    }

    @Test
    void deveRetornarErro401AoListarSemAutenticacao() throws Exception {
        mockMvc.perform(get("/pedidos"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void deveRetornarErro401AoAtualizarSemAutenticacao() throws Exception {
        mockMvc.perform(put("/pedidos/1/status?status=CONCLUIDO"))
                .andExpect(status().isUnauthorized());
    }
}
