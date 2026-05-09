package gestionPro.backend.net.security;
 
import gestionPro.backend.net.utilisateur.Utilisateur;
import gestionPro.backend.net.utilisateur.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import java.util.List;
 
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
 
    private final UtilisateurRepository utilisateurRepository;
 
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Utilisateur utilisateur = utilisateurRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("Utilisateur introuvable : " + username));
 
        return new org.springframework.security.core.userdetails.User(
            utilisateur.getUsername(),
            utilisateur.getPassword(),
            List.of(new SimpleGrantedAuthority(utilisateur.getRole()))
        );
    }
}