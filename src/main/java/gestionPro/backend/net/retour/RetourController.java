package gestionPro.backend.net.retour;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/retours")
@RequiredArgsConstructor
public class RetourController {
 
    private final RetourService retourService;
 
    // GET /api/retours
    @GetMapping
    public ResponseEntity<List<RetourDTO>> getAll() {
        return ResponseEntity.ok(retourService.findAll());
    }
 
    // GET /api/retours/1
    @GetMapping("/{id}")
    public ResponseEntity<RetourDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(retourService.findById(id));
    }
 
    // GET /api/retours/client/1
    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<RetourDTO>> getByClient(@PathVariable Long clientId) {
        return ResponseEntity.ok(retourService.findByClient(clientId));
    }
 
    // POST /api/retours
    @PostMapping
    public ResponseEntity<RetourDTO> create(@Valid @RequestBody RetourDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(retourService.create(dto));
    }
 
    // DELETE /api/retours/1
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        retourService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
