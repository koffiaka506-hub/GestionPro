package gestionPro.backend.net.abonnement;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import gestionPro.backend.net.utilisateur.StatutAbonnement;
import gestionPro.backend.net.utilisateur.Utilisateur;
import gestionPro.backend.net.utilisateur.UtilisateurRepository;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/abonnement")
@RequiredArgsConstructor
public class AbonnementController {

    private final UtilisateurRepository utilisateurRepository;

    // ── SUPER_ADMIN : liste des abonnements EN_ATTENTE ──
    @GetMapping("/en-attente")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<List<Utilisateur>> getEnAttente() {
        return ResponseEntity.ok(
            utilisateurRepository.findByStatut(StatutAbonnement.EN_ATTENTE)
        );
    }

    // ── SUPER_ADMIN : liste de tous les abonnements ──
    @GetMapping("/tous")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<List<Utilisateur>> getTous() {
        return ResponseEntity.ok(utilisateurRepository.findAll());
    }

    // ── SUPER_ADMIN : activer un abonnement ──
    @PostMapping("/activer/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<?> activer(@PathVariable Long id) {
        Utilisateur u = utilisateurRepository.findById(id)
            .orElse(null);
        if (u == null) return ResponseEntity.badRequest()
            .body(Map.of("erreur", "Utilisateur non trouvé"));

        u.setStatut(StatutAbonnement.ACTIF);
        u.setDebutAbonnement(LocalDate.now());
        u.setFinAbonnement(LocalDate.now().plusDays(30));
        // Passe automatiquement en ADMIN de son espace
        u.setRole("ROLE_ADMIN");
        utilisateurRepository.save(u);

        return ResponseEntity.ok(Map.of("message", "Abonnement activé, compte ADMIN créé"));
    }

    // ── SUPER_ADMIN : rejeter un abonnement ──
    @PostMapping("/rejeter/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<?> rejeter(@PathVariable Long id) {
        Utilisateur u = utilisateurRepository.findById(id)
            .orElse(null);
        if (u == null) return ResponseEntity.badRequest()
            .body(Map.of("erreur", "Utilisateur non trouvé"));

        u.setStatut(StatutAbonnement.EXPIRE);
        utilisateurRepository.save(u);

        return ResponseEntity.ok(Map.of("message", "Abonnement rejeté"));
    }

    // ── SUPER_ADMIN : donner un rôle à un employé ──
    @PostMapping("/role/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<?> changerRole(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String nouveauRole = body.get("role"); // ex: "ROLE_ADMIN", "ROLE_USER"
        Utilisateur u = utilisateurRepository.findById(id).orElse(null);
        if (u == null) return ResponseEntity.badRequest()
            .body(Map.of("erreur", "Utilisateur non trouvé"));

        u.setRole(nouveauRole);
        utilisateurRepository.save(u);
        return ResponseEntity.ok(Map.of("message", "Rôle mis à jour"));
    }

    // ── Vérifier son propre statut ──
    @GetMapping("/statut")
    public ResponseEntity<?> statut(@RequestParam String email) {
        Utilisateur u = utilisateurRepository.findByEmail(email).orElse(null);
        if (u == null) return ResponseEntity.badRequest()
            .body(Map.of("erreur", "Utilisateur non trouvé"));

        return ResponseEntity.ok(Map.of(
            "statut", u.getStatut(),
            "fin", u.getFinAbonnement() != null ? u.getFinAbonnement() : "Aucun abonnement actif"
        ));
    }
}