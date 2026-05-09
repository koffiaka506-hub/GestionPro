package gestionPro.backend.net.security;
 
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import gestionPro.backend.net.utilisateur.StatutAbonnement;
import gestionPro.backend.net.utilisateur.Utilisateur;
import gestionPro.backend.net.utilisateur.UtilisateurRepository;
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
 
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;
 
    // POST /api/auth/inscription
    @PostMapping("/inscription")
    public ResponseEntity<?> inscription(@RequestBody InscriptionRequest request) {
 
        if (utilisateurRepository.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.badRequest()
                .body(Map.of("erreur", "Email déjà utilisé"));
        }
 
        if (utilisateurRepository.findByUsername(request.getUsername()).isPresent()) {
            return ResponseEntity.badRequest()
                .body(Map.of("erreur", "Nom d'utilisateur déjà utilisé"));
        }
 
        // Premier compte = ADMIN, suivants = EN_ATTENTE
        long nombreComptes = utilisateurRepository.count();
        String role = (nombreComptes == 0) ? "ROLE_ADMIN" : "ROLE_EN_ATTENTE";
 
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setUsername(request.getUsername());
        utilisateur.setEmail(request.getEmail());
        utilisateur.setPassword(passwordEncoder.encode(request.getPassword()));
        utilisateur.setRole(role);
        String statutFinal = (nombreComptes == 0) ? "ROLE_SUPER_ADMIN".equals(role) ? 
        	    StatutAbonnement.ACTIF.name() : StatutAbonnement.ACTIF.name() : StatutAbonnement.EN_ATTENTE.name();
        	utilisateur.setStatut(nombreComptes == 0 ? StatutAbonnement.ACTIF : StatutAbonnement.EN_ATTENTE);
        utilisateur.setDateInscription(LocalDateTime.now());
 
        utilisateurRepository.save(utilisateur);
 
        String message = (nombreComptes == 0)
            ? "Compte administrateur créé !"
            : "Compte créé. En attente d'activation par l'administrateur.";
 
        return ResponseEntity.ok(Map.of("message", message, "role", role));
    }
 
    // POST /api/auth/login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            Utilisateur utilisateur = utilisateurRepository
                .findByUsername(request.getUsername())
                .orElse(null);
 
            if (utilisateur == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("erreur", "Identifiants incorrects"));
            }
 
            // Bloquer les comptes EN_ATTENTE
            if ("ROLE_EN_ATTENTE".equals(utilisateur.getRole())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("erreur", "Votre compte est en attente d'activation par l'administrateur"));
            }
            
            if (utilisateur.getFinAbonnement() != null &&
            	    utilisateur.getFinAbonnement().isBefore(LocalDate.now())) {
            	    utilisateur.setStatut(StatutAbonnement.EXPIRE);
            	    utilisateurRepository.save(utilisateur);
            	    return ResponseEntity.status(HttpStatus.FORBIDDEN)
            	        .body(Map.of("erreur", "Votre abonnement a expire. Contactez l'administrateur."));
            	}
 
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    request.getUsername(),
                    request.getPassword()
                )
            );
 
            String token = jwtUtil.generateToken(request.getUsername());
 
            return ResponseEntity.ok(new LoginResponse(
                token,
                utilisateur.getUsername(),
                utilisateur.getRole(),
                utilisateur.getStatut().name()
            ));
 
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("erreur", "Identifiants incorrects"));
        }
    }
 
    // GET /api/auth/utilisateurs — liste comptes (ADMIN)
    @GetMapping("/utilisateurs")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> listeUtilisateurs() {
        List<Map<String, Object>> users = utilisateurRepository.findAll()
            .stream().map(u -> Map.<String, Object>of(
                "id", u.getId(),
                "username", u.getUsername(),
                "email", u.getEmail() != null ? u.getEmail() : "",
                "role", u.getRole(),
                "statut", u.getStatut().name()
            ))
            .collect(Collectors.toList());
        return ResponseEntity.ok(users);
    }
 
    // PUT /api/auth/role/{id} — assigner rôle (ADMIN)
    @PutMapping("/role/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> assignerRole(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
 
        String role = body.get("role");
 
        if (!List.of("ROLE_ADMIN", "ROLE_USER", "ROLE_EN_ATTENTE").contains(role)) {
            return ResponseEntity.badRequest()
                .body(Map.of("erreur", "Rôle invalide"));
        }
 
        Utilisateur utilisateur = utilisateurRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));
 
        utilisateur.setRole(role);
        utilisateurRepository.save(utilisateur);
 
        return ResponseEntity.ok(Map.of(
            "message", "Rôle mis à jour",
            "username", utilisateur.getUsername(),
            "role", role
        ));
    }
}
 
@Data @NoArgsConstructor @AllArgsConstructor
class InscriptionRequest {
    private String username;
    private String email;
    private String password;
}
 
@Data @NoArgsConstructor @AllArgsConstructor
class LoginRequest {
    private String username;
    private String password;
}
 
@Data @NoArgsConstructor @AllArgsConstructor
class LoginResponse {
    private String token;
    private String username;
    private String role;
    private String statut;
}