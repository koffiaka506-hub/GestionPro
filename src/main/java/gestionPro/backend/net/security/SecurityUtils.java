package gestionPro.backend.net.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import gestionPro.backend.net.utilisateur.Utilisateur;
import gestionPro.backend.net.utilisateur.UtilisateurRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SecurityUtils {
 
    private final UtilisateurRepository utilisateurRepository;
 
    // Récupère l'utilisateur connecté depuis le token JWT
    public Utilisateur getUtilisateurConnecte() {
        String username = SecurityContextHolder.getContext()
            .getAuthentication()
            .getName();
 
        return utilisateurRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("Utilisateur connecté introuvable"));
    }
}