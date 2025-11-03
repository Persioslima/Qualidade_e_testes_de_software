package br.com.persio;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class UsuarioTest {

    @Test
    void deveCriarUsuarioComNomeValido() {
        Usuario usuario = new Usuario("Persio");
        Assertions.assertEquals("Persio", usuario.getNome());
    }

    @Test
    void naoDevePermitirNomeNuloOuVazio() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Usuario(null));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Usuario(""));
    }

    @Test
    void usuariosComMesmoNomeSaoIguais() {
        Usuario u1 = new Usuario("Persio");
        Usuario u2 = new Usuario("persio");
        Assertions.assertEquals(u1, u2);
    }
}
