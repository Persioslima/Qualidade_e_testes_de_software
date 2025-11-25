package br.restaurante.service;

import br.restaurante.dto.LoginRequest; // Adicione esta importação
import br.restaurante.model.Endereco;
import br.restaurante.model.Restaurante;
import br.restaurante.repository.RestauranteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; // Adicione esta importação

import java.util.InputMismatchException;
import java.util.List;
import java.util.Optional;

@Service
public class RestauranteService {

    @Autowired
    private RestauranteRepository restauranteRepository;

    @Autowired
    private ViaCepService viaCepService;

    @Autowired // Injete o password encoder
    private BCryptPasswordEncoder passwordEncoder;

    public Restaurante cadastrarRestaurante(Restaurante restaurante) {
        // ... (restante das validações)
        if (restaurante.getNome() == null || restaurante.getNome().isBlank() ||
                restaurante.getCnpj() == null || restaurante.getCnpj().isBlank() ||
                restaurante.getEmail() == null || restaurante.getEmail().isBlank() ||
                restaurante.getSenha() == null || restaurante.getSenha().isBlank()) {
            throw new InputMismatchException("Os campos Nome, CNPJ, Email e Senha são obrigatórios.");
        }
        // ...

        // 4. Criptografar a senha antes de salvar
        String senhaHash = passwordEncoder.encode(restaurante.getSenha());
        restaurante.setSenha(senhaHash);

        // ... (restante do código)

        // 5. Salvar a entidade no banco de dados
        return restauranteRepository.save(restaurante);
    }

    /**
     * Valida as credenciais do restaurante para o login.
     * @param loginRequest Objeto com email e senha.
     * @return O objeto Restaurante se as credenciais estiverem corretas, ou null.
     */
    public Restaurante loginRestaurante(LoginRequest loginRequest) {
        Optional<Restaurante> restauranteOptional = restauranteRepository.findByEmail(loginRequest.getEmail());

        if (restauranteOptional.isPresent()) {
            Restaurante restaurante = restauranteOptional.get();
            // Comparar a senha fornecida com a senha criptografada do banco de dados
            if (passwordEncoder.matches(loginRequest.getSenha(), restaurante.getSenha())) {
                return restaurante;
            }
        }
        return null; // Retorna nulo se o email não for encontrado ou a senha estiver incorreta
    }

    // ... (restante dos métodos existentes)

    // O método 'atualizarRestaurante' precisa ser ajustado para não sobrescrever a senha
    // sem uma verificação. Isso geralmente requer uma lógica mais complexa, mas
    // por enquanto, vamos apenas garantir que a senha não seja atualizada de forma acidental.
    public Optional<Restaurante> atualizarRestaurante(Long id, Restaurante restauranteAtualizado) {
        return restauranteRepository.findById(id).map(restauranteExistente -> {
            // ... (copiar todos os outros campos, exceto a senha)
            restauranteExistente.setNome(restauranteAtualizado.getNome());
            restauranteExistente.setCnpj(restauranteAtualizado.getCnpj());
            restauranteExistente.setEmail(restauranteAtualizado.getEmail());
            restauranteExistente.setTelefone(restauranteAtualizado.getTelefone());
            // ... e os outros campos

            // A senha não deve ser atualizada aqui. Geralmente, existe um endpoint
            // separado para isso.

            return restauranteRepository.save(restauranteExistente);
        });
    }

    public List<Restaurante> buscarTodos() {
        return restauranteRepository.findAll();
    }

    public Optional<Restaurante> buscarPorId(Long id) {
        return restauranteRepository.findById(id);
    }

    public boolean deletarRestaurante(Long id) {
        Optional<Restaurante> restaurante = restauranteRepository.findById(id);
        if (restaurante.isPresent()) {
            restauranteRepository.deleteById(id);
            return true;
        }
        return false;
    }
}