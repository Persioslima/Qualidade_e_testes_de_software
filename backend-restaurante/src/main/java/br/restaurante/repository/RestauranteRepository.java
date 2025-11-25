package br.restaurante.repository;

import br.restaurante.model.Restaurante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RestauranteRepository extends JpaRepository<Restaurante, Long> {

    // O Spring Data JPA já fornece os métodos CRUD básicos:
    // - save()
    // - findById()
    // - findAll()
    // - deleteById()

    // Você pode adicionar métodos de busca personalizados aqui.
    // Exemplo: encontrar um restaurante pelo CNPJ (que é único).
    Optional<Restaurante> findByCnpj(String cnpj);

    // Exemplo: encontrar restaurantes por cidade e estado.
    List<Restaurante> findByCidadeAndEstado(String cidade, String estado);

    // Exemplo: buscar por nome (ignorando maiúsculas/minúsculas).
    List<Restaurante> findByNomeContainingIgnoreCase(String nome);

    Optional<Restaurante> findByEmail(String email);
}