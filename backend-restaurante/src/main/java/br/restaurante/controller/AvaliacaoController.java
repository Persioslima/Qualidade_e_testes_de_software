package br.restaurante.controller;

import br.restaurante.model.Avaliacao;
import br.restaurante.service.AvaliacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/avaliacoes", produces = "application/json")
public class AvaliacaoController {

    @Autowired
    private AvaliacaoService avaliacaoService;

    @PostMapping
    public ResponseEntity<?> criarAvaliacao(@RequestBody Avaliacao avaliacao, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Você precisa estar logado para fazer uma avaliação."));
        }

        String emailDoCliente = userDetails.getUsername();

        try {
            // Busca o cliente pelo email do principal
            Long clienteId = avaliacaoService
                    .buscarClienteIdPorEmail(emailDoCliente)
                    .orElseThrow(() -> new InputMismatchException("Cliente não encontrado para o usuário autenticado."));

            Avaliacao novaAvaliacao = avaliacaoService.cadastrarAvaliacao(avaliacao, clienteId);
            return ResponseEntity.status(HttpStatus.CREATED).body(novaAvaliacao);
        } catch (InputMismatchException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping("/{restauranteId}")
    public ResponseEntity<?> buscarAvaliacoesPorRestaurante(@PathVariable Long restauranteId) {
        try {
            List<Avaliacao> avaliacoes = avaliacaoService.buscarAvaliacoesPorRestauranteId(restauranteId);
            return ResponseEntity.ok(avaliacoes);
        } catch (InputMismatchException e) {
            // Retorna 404 Not Found se o restaurante não existir
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<List<Avaliacao>> listarTodasAvaliacoes() {
        List<Avaliacao> listaDeAvaliacoes = avaliacaoService.buscarTodasAsAvaliacoes();
        return ResponseEntity.ok(listaDeAvaliacoes);
    }

    // Você também pode adicionar outros endpoints, como buscar uma avaliação por ID,
    // ou deletar uma avaliação, usando os métodos que o JpaRepository oferece.
    // Exemplo:
    // @GetMapping("/detalhes/{id}")
    // public ResponseEntity<Avaliacao> buscarPorId(@PathVariable Long id) {
    //     return avaliacaoService.buscarPorId(id)
    //         .map(ResponseEntity::ok)
    //         .orElseGet(() -> ResponseEntity.notFound().build());
    // }
}