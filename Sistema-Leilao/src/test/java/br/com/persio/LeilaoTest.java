package br.com.persio;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class LeilaoTest {

    @Test
    void deveAceitarUmLanceValido() {
        Leilao leilao = new Leilao("Carro antigo");
        Usuario usuario = new Usuario("Persio");
        Lance lance = new Lance(usuario, 1000.0);

        leilao.propoe(lance);

        Assertions.assertEquals(1, leilao.getLances().size());
        Assertions.assertEquals(1000.0, leilao.getLances().get(0).getValor());
    }

    @Test
    void naoDeveAceitarLanceMenorQueUltimo() {
        Leilao leilao = new Leilao("Notebook");
        Usuario usuario1 = new Usuario("Persio");
        Usuario usuario2 = new Usuario("João");

        leilao.propoe(new Lance(usuario1, 500.0));

        Assertions.assertThrows(IllegalArgumentException.class, () ->
                leilao.propoe(new Lance(usuario2, 400.0))
        );
    }

    @Test
    void naoDeveAceitarMaisDeCincoLancesDoMesmoUsuario() {
        Leilao leilao = new Leilao("Relógio de ouro");
        Usuario usuario = new Usuario("Persio");

        for (int i = 1; i <= 5; i++) {
            leilao.propoe(new Lance(usuario, i * 100.0));
        }

        Assertions.assertThrows(IllegalArgumentException.class, () ->
                leilao.propoe(new Lance(usuario, 600.0))
        );
    }

    @Test
    void deveLancarErroSeLanceForNulo() {
        Leilao leilao = new Leilao("Cadeira antiga");
        Assertions.assertThrows(IllegalArgumentException.class, () -> leilao.propoe(null));
    }
}
