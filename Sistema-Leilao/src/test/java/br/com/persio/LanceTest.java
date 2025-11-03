package br.com.persio;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class LanceTest {

    @Test
    void deveCriarLanceValido() {
        Usuario usuario = new Usuario("Persio");
        Lance lance = new Lance(usuario, 300.0);

        Assertions.assertEquals("Persio", lance.getUsuario().getNome());
        Assertions.assertEquals(300.0, lance.getValor());
    }

    @Test
    void naoDevePermitirValorZeroOuNegativo() {
        Usuario usuario = new Usuario("Persio");
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Lance(usuario, 0.0));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Lance(usuario, -50.0));
    }

    @Test
    void naoDevePermitirUsuarioNulo() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Lance(null, 100.0));
    }
}
