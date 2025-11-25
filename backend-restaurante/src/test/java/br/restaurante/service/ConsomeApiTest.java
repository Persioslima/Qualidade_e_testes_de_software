package br.restaurante.service;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para ConsomeApi.
 * Aqui usamos mocks do HttpClient para simular respostas e falhas.
 */
class ConsomeApiTest {

    @Test
    void deveRetornarJsonComSucesso() throws Exception {
        // Simula o comportamento do HttpClient
        HttpClient mockClient = mock(HttpClient.class);
        HttpResponse<String> mockResponse = mock(HttpResponse.class);

        when(mockResponse.body()).thenReturn("{\"cep\":\"01001-000\",\"localidade\":\"São Paulo\"}");
        when(mockClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(mockResponse);

        // Cria uma instância real do serviço, mas usando injeção manual do mock
        ConsomeApi consomeApi = new ConsomeApi() {
            @Override
            public String retornaJson(String url) {
                try {
                    HttpResponse<String> response = mockClient.send(
                            HttpRequest.newBuilder().uri(URI.create(url)).build(),
                            HttpResponse.BodyHandlers.ofString()
                    );
                    return response.body();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        };

        String resultado = consomeApi.retornaJson("https://viacep.com.br/ws/01001000/json/");
        assertNotNull(resultado);
        assertTrue(resultado.contains("\"cep\""));
        assertTrue(resultado.contains("São Paulo"));
    }

    @Test
    void deveLancarExcecaoQuandoHttpFalhar() throws Exception {
        HttpClient mockClient = mock(HttpClient.class);
        when(mockClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenThrow(new IOException("Falha de conexão"));

        ConsomeApi consomeApi = new ConsomeApi() {
            @Override
            public String retornaJson(String url) {
                try {
                    mockClient.send(HttpRequest.newBuilder().uri(URI.create(url)).build(),
                            HttpResponse.BodyHandlers.ofString());
                    return "erro";
                } catch (IOException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        };

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                consomeApi.retornaJson("https://api.fake"));
        assertTrue(ex.getCause() instanceof IOException);
        assertEquals("Falha de conexão", ex.getCause().getMessage());
    }
}
