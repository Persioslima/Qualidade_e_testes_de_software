package br.com.persio;

public class CalculadoraFrete {

    /**
     * Calcula o frete com base no peso e na região de destino.
     *
     * @param peso   peso do pacote em quilogramas
     * @param regiao região de destino (NORTE, SUL, SUDESTE, CENTRO-OESTE, NORDESTE)
     * @return valor do frete em reais
     */
    public double calcular(double peso, String regiao) {
        if (peso <= 0) {
            throw new IllegalArgumentException("Peso inválido!");
        }

        if (regiao == null || regiao.isEmpty()) {
            throw new IllegalArgumentException("Região não informada!");
        }

        double taxaBase;

        switch (regiao.toUpperCase()) {
            case "SUL":
                taxaBase = 10.0;
                break;
            case "SUDESTE":
                taxaBase = 12.0;
                break;
            case "CENTRO-OESTE":
                taxaBase = 15.0;
                break;
            case "NORDESTE":
                taxaBase = 18.0;
                break;
            case "NORTE":
                taxaBase = 20.0;
                break;
            default:
                throw new IllegalArgumentException("Região inválida!");
        }

        // cálculo simples: taxaBase + (peso * 2.5)
        return taxaBase + (peso * 2.5);
    }
}
