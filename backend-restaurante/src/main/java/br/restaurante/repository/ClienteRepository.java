package br.restaurante.repository;

import br.restaurante.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    // O Spring Data JPA já fornece os métodos CRUD básicos.
    // Aqui, podemos adicionar métodos personalizados de busca, o que é muito comum para a entidade Cliente.

    /**
     * Busca um cliente pelo seu CPF.
     * O CPF é um campo único, então o retorno é um Optional.
     * @param cpf O CPF do cliente.
     * @return Um Optional contendo o cliente, se encontrado.
     */
    Optional<Cliente> findByCpf(String cpf);

    /**
     * Busca um cliente pelo seu email.
     * O email também é um campo único, então o retorno é um Optional.
     * @param email O email do cliente.
     * @return Um Optional contendo o cliente, se encontrado.
     */
    Optional<Cliente> findByEmail(String email);

    /**
     * Busca todos os clientes que aceitaram a proteção de dados.
     * @param aceitaProtecaoDados O status de aceite da proteção de dados.
     * @return Uma lista de clientes que correspondem ao status.
     */
    List<Cliente> findByAceitaProtecaoDados(boolean aceitaProtecaoDados);
}