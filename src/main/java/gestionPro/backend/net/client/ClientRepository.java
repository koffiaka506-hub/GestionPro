package gestionPro.backend.net.client;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {
	 
    // Tous les clients d'un utilisateur
    List<Client> findByUtilisateurId(Long utilisateurId);
 
    // Chercher par téléphone pour un utilisateur
    Optional<Client> findByTelephoneAndUtilisateurId(String telephone, Long utilisateurId);
}
