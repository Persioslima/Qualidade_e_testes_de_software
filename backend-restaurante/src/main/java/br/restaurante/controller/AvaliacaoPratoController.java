package br.restaurante.controller;

import br.restaurante.model.AvaliacaoPrato;
import br.restaurante.service.AvaliacaoPratoService;
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
@RequestMapping(value = "/avaliacoes-prato", produces = "application/json")
public class AvaliacaoPratoController {

    @Autowired
    private AvaliacaoPratoService avaliacaoPratoService;

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody AvaliacaoPrato avaliacaoPrato,
                                   @AuthenticationPrincipal UserDetails principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "É necessário estar logado."));
        }
        try {
            Long itemId = avaliacaoPrato.getPrato() != null ? avaliacaoPrato.getPrato().getId() : null;
            if (itemId == null || !avaliacaoPratoService.clienteConcluiuPedidoComItem(principal.getUsername(), itemId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Map.of("message", "Apenas clientes com pedidos concluídos contendo este item podem avaliar."));
            }
            AvaliacaoPrato salvo = avaliacaoPratoService.cadastrarAvaliacaoPrato(avaliacaoPrato, principal.getUsername());
            return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
        } catch (InputMismatchException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping("/item/{itemId}")
    public ResponseEntity<?> listarPorItem(@PathVariable Long itemId) {
        try {
            List<AvaliacaoPrato> list = avaliacaoPratoService.buscarAvaliacoesPorPratoId(itemId);
            return ResponseEntity.ok(list);
        } catch (InputMismatchException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<List<AvaliacaoPrato>> listarTodos() {
        List<AvaliacaoPrato> lista = avaliacaoPratoService.buscarTodasAvaliacoes();
        return ResponseEntity.ok(lista);
    }
}

