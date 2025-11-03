package br.com.persio;

import java.util.Comparator;

/**
 * Avaliador é responsável por analisar os lances de um leilão
 * e determinar o maior e o menor valor ofertado.
 */
public class Avaliador {

    private double maiorLance = Double.NEGATIVE_INFINITY;
    private double menorLance = Double.POSITIVE_INFINITY;

    public void avaliar(Leilao leilao) {
        if (leilao == null) {
            throw new IllegalArgumentException("Leilão não pode ser nulo.");
        }

        if (leilao.getLances().isEmpty()) {
            throw new IllegalArgumentException("Não há lances para avaliar.");
        }

        maiorLance = leilao.getLances().stream()
                .max(Comparator.comparingDouble(Lance::getValor))
                .get().getValor();

        menorLance = leilao.getLances().stream()
                .min(Comparator.comparingDouble(Lance::getValor))
                .get().getValor();
    }

    public double getMaiorLance() {
        return maiorLance;
    }

    public double getMenorLance() {
        return menorLance;
    }
}
