package br.restaurante.service;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Teste de contrato da interface IConverteDados.
 * Verifica se uma implementação mockada respeita o tipo genérico.
 */
class IConverteDadosTest {

    interface MockData { String valor(); }

    @Test
    void devePermitirConversaoGenericaComMock() {
        IConverteDados mockConverte = mock(IConverteDados.class);

        MockData dadoFalso = () -> "ok";
        when(mockConverte.converteDados(anyString(), eq(MockData.class)))
                .thenReturn(dadoFalso);

        MockData resultado = mockConverte.converteDados("{}", MockData.class);

        assertNotNull(resultado);
        assertEquals("ok", resultado.valor());
        verify(mockConverte, times(1)).converteDados(anyString(), eq(MockData.class));
    }
}
