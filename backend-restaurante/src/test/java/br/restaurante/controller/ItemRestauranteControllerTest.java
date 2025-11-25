package br.restaurante.controller;

import br.restaurante.model.ItemRestaurante;
import br.restaurante.service.FileStorageService;
import br.restaurante.service.ItemRestauranteService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * ✅ Testes unitários do ItemRestauranteController.
 * Testa CRUD e upload isoladamente via MockMvc.
 */
@WebMvcTest(ItemRestauranteController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ItemRestauranteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemRestauranteService itemRestauranteService;

    @MockBean
    private FileStorageService fileStorageService;

    @Test
    void deveCadastrarItemComSucesso() throws Exception {
        ItemRestaurante mockItem = new ItemRestaurante();
        mockItem.setId(1L);
        mockItem.setNome("Pizza");

        Mockito.when(itemRestauranteService.cadastrarItem(any(ItemRestaurante.class)))
                .thenReturn(mockItem);

        mockMvc.perform(post("/itens")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"Pizza\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Pizza"));
    }

    @Test
    void deveBuscarItemPorId() throws Exception {
        ItemRestaurante mockItem = new ItemRestaurante();
        mockItem.setId(2L);
        mockItem.setNome("Hamburguer");

        Mockito.when(itemRestauranteService.buscarItemPorId(2L))
                .thenReturn(Optional.of(mockItem));

        mockMvc.perform(get("/itens/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Hamburguer"));
    }

    @Test
    void deveBuscarTodosItens() throws Exception {
        ItemRestaurante i1 = new ItemRestaurante();
        i1.setId(1L);
        i1.setNome("Pizza");

        ItemRestaurante i2 = new ItemRestaurante();
        i2.setId(2L);
        i2.setNome("Hamburguer");

        Mockito.when(itemRestauranteService.buscarTodosItens())
                .thenReturn(List.of(i1, i2));

        mockMvc.perform(get("/itens"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void deveAtualizarItem() throws Exception {
        ItemRestaurante atualizado = new ItemRestaurante();
        atualizado.setId(3L);
        atualizado.setNome("Suco Natural");

        Mockito.when(itemRestauranteService.atualizarItem(eq(3L), any(ItemRestaurante.class)))
                .thenReturn(Optional.of(atualizado));

        mockMvc.perform(put("/itens/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"Suco Natural\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Suco Natural"));
    }

    @Test
    void deveDeletarItem() throws Exception {
        mockMvc.perform(delete("/itens/4"))
                .andExpect(status().isNoContent());

        Mockito.verify(itemRestauranteService).deletarItem(4L);
    }

    @Test
    void deveFazerUploadDeImagem() throws Exception {
        MockMultipartFile mockFile = new MockMultipartFile(
                "file", "foto.png", "image/png", "conteudo".getBytes());

        Mockito.when(fileStorageService.saveFile(any(), eq("itens")))
                .thenReturn("http://localhost/itens/foto.png");

        mockMvc.perform(multipart("/itens/upload").file(mockFile))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.url").value("http://localhost/itens/foto.png"));
    }

    @Test
    void deveBuscarItensPorRestaurante() throws Exception {
        ItemRestaurante i1 = new ItemRestaurante();
        i1.setId(1L);
        i1.setNome("Pizza");

        Mockito.when(itemRestauranteService.buscarItensPorRestaurante(99L))
                .thenReturn(List.of(i1));

        mockMvc.perform(get("/itens/restaurante/99"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Pizza"));
    }
}
