package br.restaurante.controller;

import br.restaurante.dto.PedidoRequest;
import br.restaurante.model.Pedido;
import br.restaurante.service.PedidoService;
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
@RequestMapping("/pedidos")
public class PedidoController {

    @Autowired private PedidoService pedidoService;

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody PedidoRequest request, @AuthenticationPrincipal UserDetails principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "É necessário estar logado para criar pedidos."));
        }
        try {
            Pedido pedido = pedidoService.criarPedidoParaCliente(principal.getUsername(), request);
            return ResponseEntity.status(HttpStatus.CREATED).body(pedido);
        } catch (InputMismatchException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<?> listar(@AuthenticationPrincipal UserDetails principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "É necessário estar logado."));
        }
        List<Pedido> pedidos = pedidoService.listarPedidosDoCliente(principal.getUsername());
        return ResponseEntity.ok(pedidos);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> atualizarStatus(@PathVariable Long id, @RequestParam String status, @AuthenticationPrincipal UserDetails principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "É necessário estar logado."));
        }
        try {
            Pedido atualizado = pedidoService.atualizarStatus(id, status, principal.getUsername());
            return ResponseEntity.ok(atualizado);
        } catch (InputMismatchException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", e.getMessage()));
        }
    }
}

