package isolated;

import br.restaurante.controller.GenericController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/dummy")
public class DummyController extends GenericController<String, Object> {

    public DummyController() {
        super(null);
    }

    @PostMapping
    @Override
    public ResponseEntity<String> create(@RequestBody String entity) {
        return ResponseEntity.status(201).body("created");
    }

    @GetMapping("/{id}")
    @Override
    public ResponseEntity<Optional<String>> read(@PathVariable Long id) {
        return ResponseEntity.ok(Optional.of("read"));
    }

    @GetMapping
    @Override
    public ResponseEntity<List<String>> readAll() {
        return ResponseEntity.ok(List.of("a", "b"));
    }

    @PutMapping("/{id}")
    @Override
    public ResponseEntity<String> update(@PathVariable Long id, @RequestBody String entity) {
        return ResponseEntity.ok("updated");
    }

    @DeleteMapping("/{id}")
    @Override
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return ResponseEntity.noContent().build();
    }
}
