package br.restaurante.controller;

import br.restaurante.dto.LoginRequest;
import br.restaurante.model.Cliente;
import br.restaurante.service.ClienteService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * ✅ Testes de unidade da camada Controller (ClienteController)
 * Estratégia: MockMvc + Mockito, segurança desativada.
 */
@WebMvcTest(ClienteController.class)
@AutoConfigureMockMvc(addFilters = false)
class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClienteService clienteService;

    // ======================
    // LOGIN / LOGOUT
    // ======================

    @Test
    void deveRealizarLoginComSucesso() throws Exception {
        Cliente cliente = new Cliente();
        cliente.setEmail("cliente@teste.com");
        cliente.setSenha("123");

        when(clienteService.loginCliente(any(LoginRequest.class))).thenReturn(cliente);

        mockMvc.perform(post("/clientes/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"cliente@teste.com\",\"senha\":\"123\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Login realizado com sucesso"))
                .andExpect(jsonPath("$.email").value("cliente@teste.com"));
    }

    @Test
    void deveFalharLoginComCredenciaisInvalidas() throws Exception {
        when(clienteService.loginCliente(any(LoginRequest.class))).thenReturn(null);

        mockMvc.perform(post("/clientes/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"invalido@teste.com\",\"senha\":\"errada\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Email ou senha incorretos."));
    }

    @Test
    void deveRealizarLogoutComSucesso() throws Exception {
        mockMvc.perform(post("/clientes/logout"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Logout realizado com sucesso"));
    }

    // ======================
    // CRUD
    // ======================

    @Test
    void deveCriarClienteComSucesso() throws Exception {
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNome("João");
        cliente.setEmail("joao@teste.com");

        when(clienteService.cadastrarCliente(any(Cliente.class))).thenReturn(cliente);

        mockMvc.perform(post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"João\",\"cpf\":\"12345678900\",\"email\":\"joao@teste.com\",\"senha\":\"123\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("João"))
                .andExpect(jsonPath("$.email").value("joao@teste.com"));
    }

    @Test
    void deveRetornarConflictQuandoEmailJaExistir() throws Exception {
        when(clienteService.cadastrarCliente(any(Cliente.class)))
                .thenThrow(new IllegalArgumentException("Email já cadastrado."));

        mockMvc.perform(post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"Ana\",\"cpf\":\"12345678900\",\"email\":\"ana@teste.com\",\"senha\":\"123\"}"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Email já cadastrado."));
    }

    @Test
    void deveRetornarBadRequestParaCamposInvalidos() throws Exception {
        when(clienteService.cadastrarCliente(any(Cliente.class)))
                .thenThrow(new java.util.InputMismatchException("O CPF deve conter 11 dígitos."));

        mockMvc.perform(post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"Ana\",\"cpf\":\"123\",\"email\":\"ana@teste.com\",\"senha\":\"123\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("O CPF deve conter 11 dígitos."));
    }

    @Test
    void deveListarTodosOsClientes() throws Exception {
        Cliente c1 = new Cliente();
        c1.setNome("João");
        Cliente c2 = new Cliente();
        c2.setNome("Maria");

        when(clienteService.buscarTodos()).thenReturn(List.of(c1, c2));

        mockMvc.perform(get("/clientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("João"))
                .andExpect(jsonPath("$[1].nome").value("Maria"));
    }

    @Test
    void deveBuscarClientePorIdExistente() throws Exception {
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNome("Carlos");

        when(clienteService.buscarPorId(1L)).thenReturn(Optional.of(cliente));

        mockMvc.perform(get("/clientes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Carlos"));
    }

    @Test
    void deveRetornarNotFoundParaClienteInexistente() throws Exception {
        when(clienteService.buscarPorId(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/clientes/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveAtualizarClienteComSucesso() throws Exception {
        Cliente atualizado = new Cliente();
        atualizado.setId(1L);
        atualizado.setNome("Maria Atualizada");

        when(clienteService.atualizarCliente(eq(1L), any(Cliente.class)))
                .thenReturn(Optional.of(atualizado));

        mockMvc.perform(put("/clientes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"Maria Atualizada\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Maria Atualizada"));
    }

    @Test
    void deveRetornarNotFoundAoAtualizarClienteInexistente() throws Exception {
        when(clienteService.atualizarCliente(eq(99L), any(Cliente.class)))
                .thenReturn(Optional.empty());

        mockMvc.perform(put("/clientes/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"Teste\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveDeletarClienteComSucesso() throws Exception {
        when(clienteService.deletarCliente(1L)).thenReturn(true);

        mockMvc.perform(delete("/clientes/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deveRetornarNotFoundAoDeletarClienteInexistente() throws Exception {
        when(clienteService.deletarCliente(99L)).thenReturn(false);

        mockMvc.perform(delete("/clientes/99"))
                .andExpect(status().isNotFound());
    }
}
