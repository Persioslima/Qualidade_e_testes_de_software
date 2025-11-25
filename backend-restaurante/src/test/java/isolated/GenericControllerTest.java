package isolated;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * ✅ Teste funcional do GenericController isolado, com contexto explícito.
 */
@WebMvcTest(controllers = DummyController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(DummyController.class)
@ContextConfiguration(classes = { DummyController.class }) // 👈 Adicione esta linha
public class GenericControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void deveResponderComCreatedAoCriar() throws Exception {
        mockMvc.perform(post("/dummy")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isCreated());
    }

    @Test
    void deveResponderComNoContentAoDeletar() throws Exception {
        mockMvc.perform(delete("/dummy/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deveRetornarOkNosDemaisEndpoints() throws Exception {
        mockMvc.perform(get("/dummy")).andExpect(status().isOk());
        mockMvc.perform(get("/dummy/1")).andExpect(status().isOk());
        mockMvc.perform(put("/dummy/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk());
    }
}
