package br.restaurante.repository;

import br.restaurante.model.Avaliacao;
import br.restaurante.model.Restaurante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AvaliacaoRepository extends JpaRepository<Avaliacao, Long> {

    // O JpaRepository já fornece os métodos CRUD básicos (save, findById, findAll, etc.).
    // Podemos adicionar métodos personalizados para buscar avaliações de forma específica.

    /**
     * Busca todas as avaliações de um restaurante específico.
     * @param restaurante O objeto Restaurante para o qual as avaliações são procuradas.
     * @return Uma lista de avaliações do restaurante.
     */
    List<Avaliacao> findByRestaurante(Restaurante restaurante);

    /**
     * Busca avaliações por uma nota específica, em um restaurante específico.
     * @param restaurante O objeto Restaurante.
     * @param nota A nota da avaliação.
     * @return Uma lista de avaliações que correspondem aos critérios.
     */
    List<Avaliacao> findByRestauranteAndNota(Restaurante restaurante, Double nota);

    /**
     * Busca todas as avaliações com nota maior ou igual a um valor.
     * @param nota O valor mínimo da nota.
     * @return Uma lista de avaliações.
     */
    List<Avaliacao> findByNotaGreaterThanEqual(Double nota);
}