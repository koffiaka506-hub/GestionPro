package gestionPro.backend.net.retour;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
public interface RetourRepository extends JpaRepository<Retour, Long> {
	 
    List<Retour> findByUtilisateurId(Long utilisateurId);
 
    List<Retour> findByClientIdAndUtilisateurId(Long clientId, Long utilisateurId);
 
    @Query("SELECT SUM(r.montantRembourse) FROM Retour r WHERE r.utilisateur.id = :uid AND r.dateRetour BETWEEN :debut AND :fin")
    Double totalRembourse(@Param("uid") Long uid, @Param("debut") LocalDate debut, @Param("fin") LocalDate fin);
 
    @Query("SELECT COUNT(r) FROM Retour r WHERE r.utilisateur.id = :uid AND r.dateRetour = :date")
    Long countByDate(@Param("uid") Long uid, @Param("date") LocalDate date);
}