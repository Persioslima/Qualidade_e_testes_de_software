package br.restaurante.service;

import br.restaurante.model.Avaliacao;
import br.restaurante.model.Cliente;
import br.restaurante.model.Restaurante;
import br.restaurante.repository.AvaliacaoRepository;
import br.restaurante.repository.ClienteRepository; // Importação necessária
import br.restaurante.repository.RestauranteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Optional;

@Service
public class AvaliacaoService {

    @Autowired
    private AvaliacaoRepository avaliacaoRepository;

    @Autowired
    private RestauranteRepository restauranteRepository;

    @Autowired // Linha essencial para que o serviço funcione
    private ClienteRepository clienteRepository;

    /**
     * Realiza o cadastro de uma nova avaliação de restaurante, associando ao cliente logado.
     * @param avaliacao O objeto Avaliacao a ser salvo.
     * @param clienteId O ID do cliente logado.
     * @return O objeto Avaliacao salvo com o ID gerado.
     * @throws InputMismatchException se os campos obrigatórios não forem preenchidos, a nota for inválida ou o restaurante não for encontrado.
     */
    public Avaliacao cadastrarAvaliacao(Avaliacao avaliacao, Long clienteId) {
        // --- Programação Defensiva: Validação de dados de entrada ---
        if (avaliacao.getNota() == null) {
            throw new InputMismatchException("A nota da avaliação é obrigatória.");
        }
        if (avaliacao.getNota() < 1 || avaliacao.getNota() > 5) {
            throw new InputMismatchException("A nota deve ser um valor entre 1 e 5.");
        }
        if (avaliacao.getRestaurante() == null || avaliacao.getRestaurante().getId() == null) {
            throw new InputMismatchException("É necessário associar a avaliação a um restaurante.");
        }

        Optional<Restaurante> restauranteExistente = restauranteRepository.findById(avaliacao.getRestaurante().getId());
        if (restauranteExistente.isEmpty()) {
            throw new InputMismatchException("Restaurante não encontrado. Verifique o ID do restaurante.");
        }

        Optional<Cliente> clienteExistente = clienteRepository.findById(clienteId);
        if (clienteExistente.isEmpty()) {
            throw new InputMismatchException("Cliente não encontrado. Você precisa estar logado.");
        }

        avaliacao.setRestaurante(restauranteExistente.get());
        avaliacao.setCliente(clienteExistente.get());
        avaliacao.setDataAvaliacao(LocalDateTime.now());

        return avaliacaoRepository.save(avaliacao);
    }

    public List<Avaliacao> buscarAvaliacoesPorRestauranteId(Long restauranteId) {
        Optional<Restaurante> restaurante = restauranteRepository.findById(restauranteId);
        if (restaurante.isEmpty()) {
            throw new InputMismatchException("Restaurante não encontrado.");
        }
        return avaliacaoRepository.findByRestaurante(restaurante.get());
    }

    public java.util.Optional<Long> buscarClienteIdPorEmail(String email) {
        return clienteRepository.findByEmail(email).map(Cliente::getId);
    }

    public List<Avaliacao> buscarTodasAsAvaliacoes() {
        return avaliacaoRepository.findAll();
    }
}