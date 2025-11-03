package br.com.persio;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CalculadoraFreteTest {

    @Test
    void deveCalcularFreteParaRegiaoSulComPesoValido() {
        CalculadoraFrete calc = new CalculadoraFrete();
        double valor = calc.calcular(4.0, "SUL");
        Assertions.assertEquals(20.0, valor);
    }

    @Test
    void deveLancarErroParaPesoInvalido() {
        CalculadoraFrete calc = new CalculadoraFrete();
        Assertions.assertThrows(IllegalArgumentException.class, () -> calc.calcular(0, "SUL"));
    }

    @Test
    void deveLancarErroParaRegiaoInvalida() {
        CalculadoraFrete calc = new CalculadoraFrete();
        Assertions.assertThrows(IllegalArgumentException.class, () -> calc.calcular(5, "OESTE"));
    }

    @Test
    void deveLancarErroParaRegiaoNulaOuVazia() {
        CalculadoraFrete calc = new CalculadoraFrete();
        Assertions.assertThrows(IllegalArgumentException.class, () -> calc.calcular(5, null));
        Assertions.assertThrows(IllegalArgumentException.class, () -> calc.calcular(5, ""));
    }

    @Test
    void deveCalcularFreteParaRegiaoNordeste() {
        CalculadoraFrete calc = new CalculadoraFrete();
        double valor = calc.calcular(3.0, "NORDESTE");
        Assertions.assertEquals(25.5, valor); // 18.0 + (3.0 * 2.5)
    }

    @Test
    void deveCalcularFreteParaRegiaoNorte() {
        CalculadoraFrete calc = new CalculadoraFrete();
        double valor = calc.calcular(2.0, "NORTE");
        Assertions.assertEquals(25.0, valor); // 20.0 + (2.0 * 2.5)
    }

}
