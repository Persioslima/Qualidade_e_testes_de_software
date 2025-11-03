package br.com.persio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Representa um leilão contendo vários lances de diferentes usuários.
 * Regras de negócio:
 * - Cada usuário pode dar no máximo 5 lances.
 * - Não aceita lances com valor menor que o último.
 */
public class Leilao {

    private String descricao;
    private List<Lance> lances = new ArrayList<>();

    public Leilao(String descricao) {
        if (descricao == null || descricao.isBlank()) {
            throw new IllegalArgumentException("Descrição do leilão não pode ser vazia ou nula.");
        }
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    public List<Lance> getLances() {
        return Collections.unmodifiableList(lances);
    }

    public void propoe(Lance lance) {
        if (lance == null) {
            throw new IllegalArgumentException("Lance não pode ser nulo.");
        }

        if (!lances.isEmpty()) {
            Lance ultimoLance = lances.get(lances.size() - 1);

            // Regra 1: valor decrescente não permitido
            if (lance.getValor() <= ultimoLance.getValor()) {
                throw new IllegalArgumentException("Lance deve ser maior que o anterior.");
            }

            // Regra 2: máximo de 5 lances por usuário
            long totalLancesUsuario = lances.stream()
                    .filter(l -> l.getUsuario().equals(lance.getUsuario()))
                    .count();

            if (totalLancesUsuario >= 5) {
                throw new IllegalArgumentException("Usuário atingiu o limite de 5 lances.");
            }
        }

        lances.add(lance);
    }
}
