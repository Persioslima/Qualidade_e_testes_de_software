package br.restaurante.service;

import br.restaurante.model.Endereco;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.InputMismatchException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ViaCepServiceTest {

    @Mock
    private ConsomeApi consomeApi;

    @Mock
    private IConverteDados conversor;

    @InjectMocks
    private ViaCepService viaCepService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // --- Cenário 1: CEP vazio ---
    @Test
    void deveLancarExcecaoQuandoCepVazio() {
        InputMismatchException ex = assertThrows(InputMismatchException.class,
                () -> viaCepService.buscaEnderecoPorCep(""));
        assertTrue(ex.getMessage().contains("CEP"));
        assertTrue(ex.getMessage().contains("vazio"));
    }

    // --- Cenário 2: CEP com formato inválido ---
    @Test
    void deveLancarExcecaoQuandoCepInvalido() {
        InputMismatchException ex = assertThrows(InputMismatchException.class,
                () -> viaCepService.buscaEnderecoPorCep("1234"));
        assertTrue(ex.getMessage().contains("8"));
        assertTrue(ex.getMessage().contains("dígitos"));
    }

    // --- Cenário 3: CEP não encontrado ---
    @Test
    void deveLancarExcecaoQuandoCepNaoEncontrado() {
        String cep = "99999999";
        String urlEsperada = "https://viacep.com.br/ws/" + cep + "/json/";

        when(consomeApi.retornaJson(urlEsperada)).thenReturn("{\"erro\": true}");
        when(conversor.converteDados(anyString(), eq(Endereco.class)))
                .thenReturn(new Endereco(null, null, null, null, null));

        InputMismatchException ex = assertThrows(InputMismatchException.class,
                () -> viaCepService.buscaEnderecoPorCep(cep));
        assertTrue(ex.getMessage().toLowerCase().contains("não encontrado"));
    }

    // --- Cenário 4: CEP válido retorna endereço ---
    @Test
    void deveRetornarEnderecoQuandoCepValido() {
        String cep = "01001000";
        String urlEsperada = "https://viacep.com.br/ws/" + cep + "/json/";
        String jsonFalso = "{\"cep\":\"01001-000\",\"logradouro\":\"Praça da Sé\",\"bairro\":\"Sé\",\"localidade\":\"São Paulo\",\"uf\":\"SP\"}";

        when(consomeApi.retornaJson(urlEsperada)).thenReturn(jsonFalso);
        when(conversor.converteDados(jsonFalso, Endereco.class))
                .thenReturn(new Endereco("01001-000", "Praça da Sé", "Sé", "São Paulo", "SP"));

        Endereco resultado = viaCepService.buscaEnderecoPorCep(cep);

        assertNotNull(resultado);
        assertEquals("01001-000", resultado.cep());
        assertEquals("São Paulo", resultado.cidade());
        verify(consomeApi, times(1)).retornaJson(urlEsperada);
        verify(conversor, times(1)).converteDados(jsonFalso, Endereco.class);
    }
}
