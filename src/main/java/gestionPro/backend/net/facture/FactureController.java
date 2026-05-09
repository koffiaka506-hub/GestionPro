package gestionPro.backend.net.facture;

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
@RequestMapping("/api/factures")
@RequiredArgsConstructor
public class FactureController {
 
    private final FactureService factureService;
 
    // GET /api/factures
    @GetMapping
    public ResponseEntity<List<FactureDTO>> getAll() {
        return ResponseEntity.ok(factureService.findAll());
    }
 
    // GET /api/factures/1
    @GetMapping("/{id}")
    public ResponseEntity<FactureDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(factureService.findById(id));
    }
 
    // GET /api/factures/client/1
    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<FactureDTO>> getByClient(@PathVariable Long clientId) {
        return ResponseEntity.ok(factureService.findByClient(clientId));
    }
 
    // POST /api/factures
    @PostMapping
    public ResponseEntity<FactureDTO> create(@Valid @RequestBody FactureDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(factureService.create(dto));
    }
 
    // DELETE /api/factures/1
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        factureService.delete(id);
        return ResponseEntity.noContent().build();
    }
}