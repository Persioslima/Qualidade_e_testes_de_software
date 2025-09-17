package com.estudo.testejunit;

public class App {
    public boolean ehNumeroPar(int n) {
        return n % 2 == 0;
    }

    public boolean contemPalavra(String frase, String palavra) {
        return frase != null && frase.contains(palavra);
    }

    public boolean ehMaiorDeIdade(int idade) {
        boolean resultado = idade >= 18;
        System.out.println("Maior idade=" + idade + ", resultado=" + resultado);
        return resultado;
    }


    public boolean isPalindromo(String texto) {
        if (texto == null) return false;
        String invertido = new StringBuilder(texto).reverse().toString();
        return texto.equalsIgnoreCase(invertido);
    }
}
