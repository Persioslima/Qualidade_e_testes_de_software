package br.restaurante.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários para ConverteDados.
 * Verifica a conversão correta de JSON em objetos e o tratamento de erros de parsing.
 */
class ConverteDadosTest {

    private final ConverteDados converteDados = new ConverteDados();

    // Classe auxiliar de teste (simula um DTO)
    public static class PessoaFake {
        public String nome;
        public int idade;
    }

    // ✅ TESTE: conversão bem-sucedida
    @Test
    void deveConverterJsonParaObjetoComSucesso() {
        String json = "{\"nome\":\"Persio\",\"idade\":30}";

        PessoaFake pessoa = converteDados.converteDados(json, PessoaFake.class);

        assertNotNull(pessoa);
        assertEquals("Persio", pessoa.nome);
        assertEquals(30, pessoa.idade);
    }

    // ❌ TESTE: JSON inválido deve lançar exceção
    @Test
    void deveLancarExcecaoAoConverterJsonInvalido() {
        String jsonInvalido = "{nome: Persio, idade: 30"; // faltando fechamento

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                converteDados.converteDados(jsonInvalido, PessoaFake.class));

        assertTrue(ex.getCause() instanceof JsonProcessingException);
    }

    // ✅ TESTE: objeto complexo com campos nulos
    @Test
    void deveConverterMesmoComCamposFaltando() {
        String jsonParcial = "{\"nome\":\"Persio\"}"; // sem idade

        PessoaFake pessoa = converteDados.converteDados(jsonParcial, PessoaFake.class);

        assertNotNull(pessoa);
        assertEquals("Persio", pessoa.nome);
        assertEquals(0, pessoa.idade); // valor default do tipo primitivo int
    }
}
