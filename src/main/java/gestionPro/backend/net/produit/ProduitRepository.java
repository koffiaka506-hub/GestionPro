package gestionPro.backend.net.produit;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProduitRepository extends JpaRepository<Produit, Long> {
	 
    // Tous les produits d'un utilisateur
    List<Produit> findByUtilisateurId(Long utilisateurId);
 
    // Produits avec stock faible d'un utilisateur
    @Query("SELECT p FROM Produit p WHERE p.utilisateur.id = :uid AND p.stock < 10 ORDER BY p.stock ASC")
    List<Produit> findStockFaibleByUtilisateur(@Param("uid") Long utilisateurId);
 
    // Chercher par nom pour un utilisateur
    List<Produit> findByNomContainingIgnoreCaseAndUtilisateurId(String nom, Long utilisateurId);
}
