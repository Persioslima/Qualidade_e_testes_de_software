package com.estudo.testejunit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AppTest {

    App app = new App();

    @Test
    @DisplayName("Deve reconhecer corretamente números pares e ímpares")
    void testEhNumeroPar() {
        int numeroPar = 2;
        int numeroImpar = 5;

        boolean resultadoPar = app.ehNumeroPar(numeroPar);
        System.out.println("Número " + numeroPar + " -> resultado: " + resultadoPar);
        assertTrue(resultadoPar,
                () -> "Número " + numeroPar + " deveria ser reconhecido como par");

        boolean resultadoImpar = app.ehNumeroPar(numeroImpar);
        System.out.println("Número " + numeroImpar + " -> resultado: " + resultadoImpar);
        assertFalse(resultadoImpar,
                () -> "Número " + numeroImpar + " não deveria ser reconhecido como par");
    }

    @Test
    @DisplayName("Deve verificar se um texto contém uma palavra específica")
    void testContemPalavra() {
        String frase = "Java é legal";
        String palavraCerta = "Java";
        String palavraErrada = "Python";

        boolean resultadoCerta = app.contemPalavra(frase, palavraCerta);
        System.out.println("Frase: \"" + frase + "\" contém \"" + palavraCerta + "\" -> " + resultadoCerta);
        assertTrue(resultadoCerta,
                () -> "A frase \"" + frase + "\" deveria conter a palavra \"" + palavraCerta + "\"");

        boolean resultadoErrada = app.contemPalavra(frase, palavraErrada);
        System.out.println("Frase: \"" + frase + "\" contém \"" + palavraErrada + "\" -> " + resultadoErrada);
        assertFalse(resultadoErrada,
                () -> "A frase \"" + frase + "\" não deveria conter a palavra \"" + palavraErrada + "\"");
    }

    @Test @DisplayName("Deve verificar corretamente se alguém é maior de idade")
    void testEhMaiorDeIdade() { int maior = 18; int menor = 15;
        assertTrue(app.ehMaiorDeIdade(maior), () -> "Idade " + maior + " deveria ser considerada maior de idade");
        assertFalse(app.ehMaiorDeIdade(menor), () -> "Idade " + menor + " deveria ser considerada menor de idade"); }

    @Test
    @DisplayName("Deve identificar corretamente palavras palíndromas")
    void testIsPalindromo() {
        String palindromo = "ovo";
        String naoPalindromo = "cachorro";

        boolean resultadoPalindromo = app.isPalindromo(palindromo);
        System.out.println("Palavra \"" + palindromo + "\" -> é palíndromo? " + resultadoPalindromo);
        assertTrue(resultadoPalindromo,
                () -> "\"" + palindromo + "\" deveria ser reconhecida como palíndromo");

        boolean resultadoNaoPalindromo = app.isPalindromo(naoPalindromo);
        System.out.println("Palavra \"" + naoPalindromo + "\" -> é palíndromo? " + resultadoNaoPalindromo);
        assertFalse(resultadoNaoPalindromo,
                () -> "\"" + naoPalindromo + "\" não deveria ser reconhecida como palíndromo");
    }
}
