package gestionPro.backend.net.security;
 
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import gestionPro.backend.net.utilisateur.StatutAbonnement;
import gestionPro.backend.net.utilisateur.Utilisateur;
import gestionPro.backend.net.utilisateur.UtilisateurRepository;

import java.io.IOException;
import java.time.LocalDate;
 
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    // ✅ AJOUT — pour vérifier le statut de l'abonnement
    private final UtilisateurRepository utilisateurRepository;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        // 1. Lire le header Authorization
        String authHeader = request.getHeader("Authorization");

        // 2. Vérifier que le header commence par "Bearer "
        //    Si absent → laisser passer (sera bloqué par Spring Security si route protégée)
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3. Extraire le token (enlever "Bearer ")
        String token = authHeader.substring(7);

        // 4. Valider le token
        if (jwtUtil.isTokenValid(token)) {
            String username = jwtUtil.extractUsername(token);

            // ✅ AJOUT — Récupérer l'utilisateur en base pour vérifier son abonnement
            Utilisateur utilisateur = utilisateurRepository
            	    .findByUsername(username)
            	    .orElse(null);

            // ✅ AJOUT — Bloquer si utilisateur introuvable
            if (utilisateur == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
                response.setContentType("application/json");
                response.getWriter().write("{\"erreur\": \"Utilisateur introuvable\"}");
                return;
            }

            // ✅ AJOUT — Vérifier si abonnement expiré (date dépassée)
            //    Si oui → mettre à jour le statut en base automatiquement
            if (utilisateur.getFinAbonnement() != null &&
                LocalDate.now().isAfter(utilisateur.getFinAbonnement())) {
                utilisateur.setStatut(StatutAbonnement.EXPIRE);
                utilisateurRepository.save(utilisateur);
            }

            // ✅ AJOUT — Bloquer si abonnement non actif (EN_ATTENTE ou EXPIRE)
            if (utilisateur.getStatut() != StatutAbonnement.ACTIF) {
                response.setStatus(402); // 402 Payment Required
                response.setContentType("application/json");
                response.getWriter().write(
                    "{\"erreur\": \"Abonnement inactif ou expiré, veuillez renouveler\"}"
                );
                return;
            }

            // 5. Authentifier l'utilisateur dans le contexte Spring Security
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities()
                );
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
       
        filterChain.doFilter(request, response);
    }
}