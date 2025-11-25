package br.restaurante.repository;

import br.restaurante.model.AvaliacaoPrato;
import br.restaurante.model.ItemRestaurante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AvaliacaoPratoRepository extends JpaRepository<AvaliacaoPrato, Long> {
    // Altere o nome do método para 'findByItemRestaurante'
    // O Spring Data JPA vai entender que é para buscar pelo campo 'itemRestaurante'
    List<AvaliacaoPrato> findByItemRestaurante(ItemRestaurante itemRestaurante);

    // Ou, se preferir buscar pelo ID do item:
    List<AvaliacaoPrato> findByItemRestauranteId(Long itemRestauranteId);
}