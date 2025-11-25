package br.restaurante.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

public abstract class GenericController<T, S> {

    protected final S service;

    public GenericController(S service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<T> create(@RequestBody T entity) {
        // Implementar a lógica de criação no serviço
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<T>> read(@PathVariable Long id) {
        // Implementar a lógica de leitura por ID no serviço
        return ResponseEntity.ok(Optional.empty());
    }

    @GetMapping
    public ResponseEntity<List<T>> readAll() {
        // Implementar a lógica de leitura de todos no serviço
        return ResponseEntity.ok(List.of());
    }

    @PutMapping("/{id}")
    public ResponseEntity<T> update(@PathVariable Long id, @RequestBody T entity) {
        // Implementar a lógica de atualização no serviço
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        // Implementar a lógica de exclusão no serviço
        return ResponseEntity.noContent().build();
    }
}