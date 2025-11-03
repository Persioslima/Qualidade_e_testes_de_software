package br.com.persio;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class AvaliadorTest {

    @Test
    void deveEncontrarMaiorEMenorLanceEmOrdemCrescente() {
        Usuario persio = new Usuario("Persio");
        Usuario joao = new Usuario("João");
        Usuario maria = new Usuario("Maria");

        Leilao leilao = new Leilao("Carro antigo");
        leilao.propoe(new Lance(persio, 100.0));
        leilao.propoe(new Lance(joao, 200.0));
        leilao.propoe(new Lance(maria, 300.0));

        Avaliador avaliador = new Avaliador();
        avaliador.avaliar(leilao);

        Assertions.assertEquals(300.0, avaliador.getMaiorLance());
        Assertions.assertEquals(100.0, avaliador.getMenorLance());
    }

    @Test
    void deveEncontrarMaiorEMenorLanceMesmoComValoresAltos() {
        Usuario persio = new Usuario("Persio");
        Usuario joao = new Usuario("João");

        Leilao leilao = new Leilao("Computador gamer");
        leilao.propoe(new Lance(persio, 500.0));
        leilao.propoe(new Lance(joao, 1500.0));

        Avaliador avaliador = new Avaliador();
        avaliador.avaliar(leilao);

        Assertions.assertEquals(1500.0, avaliador.getMaiorLance());
        Assertions.assertEquals(500.0, avaliador.getMenorLance());
    }

    @Test
    void deveLancarExcecaoSeLeilaoForNulo() {
        Avaliador avaliador = new Avaliador();
        Assertions.assertThrows(IllegalArgumentException.class, () -> avaliador.avaliar(null));
    }

    @Test
    void deveLancarExcecaoSeNaoHouverLances() {
        Leilao leilao = new Leilao("Bicicleta sem lance");
        Avaliador avaliador = new Avaliador();
        Assertions.assertThrows(IllegalArgumentException.class, () -> avaliador.avaliar(leilao));
    }

    @Test
    void deveAvaliarLeilaoComApenasUmLance() {
        Usuario persio = new Usuario("Persio");
        Leilao leilao = new Leilao("TV usada");
        leilao.propoe(new Lance(persio, 700.0));

        Avaliador avaliador = new Avaliador();
        avaliador.avaliar(leilao);

        Assertions.assertEquals(700.0, avaliador.getMaiorLance());
        Assertions.assertEquals(700.0, avaliador.getMenorLance());
    }
}
