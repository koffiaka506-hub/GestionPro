package gestionPro.backend.net.facture;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FactureRepository extends JpaRepository<Facture, Long> {
	 
    List<Facture> findByUtilisateurId(Long utilisateurId);
 
    List<Facture> findByClientIdAndUtilisateurId(Long clientId, Long utilisateurId);
 
    @Query("SELECT SUM(f.montantTotal) FROM Facture f WHERE f.utilisateur.id = :uid")
    Double calculerChiffreAffaires(@Param("uid") Long uid);
 
    @Query("SELECT SUM(f.montantTotal) FROM Facture f WHERE f.utilisateur.id = :uid AND f.dateFacture BETWEEN :debut AND :fin")
    Double calculerCAEntre(@Param("uid") Long uid, @Param("debut") LocalDate debut, @Param("fin") LocalDate fin);
 
    @Query("SELECT COUNT(f) FROM Facture f WHERE f.utilisateur.id = :uid AND f.dateFacture = :date")
    Long countByDate(@Param("uid") Long uid, @Param("date") LocalDate date);
 
    @Query("SELECT COUNT(f) FROM Facture f WHERE f.utilisateur.id = :uid AND f.dateFacture BETWEEN :debut AND :fin")
    Long countBetween(@Param("uid") Long uid, @Param("debut") LocalDate debut, @Param("fin") LocalDate fin);
}