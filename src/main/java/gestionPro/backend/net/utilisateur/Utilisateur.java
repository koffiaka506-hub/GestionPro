package gestionPro.backend.net.utilisateur;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "utilisateurs")
public class Utilisateur {

	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    private String username;

	    @Column(unique = true)
	    private String email;

	    private String password;
	    private String whatsappPhone;
	    private String role; // ex: "ROLE_ADMIN", "ROLE_USER"
	    
	 // ✅ AJOUT — statut de l'abonnement (EN_ATTENTE par défaut à l'inscription)
	    @Enumerated(EnumType.STRING)
	    @Column(nullable = false)
	    private StatutAbonnement statut = StatutAbonnement.EN_ATTENTE;

	    // ✅ AJOUT — date de début de l'abonnement (renseignée après paiement)
	    private LocalDate debutAbonnement;

	    // ✅ AJOUT — date d'expiration de l'abonnement (début + 30 jours)
	    private LocalDate finAbonnement;

	    // ✅ AJOUT — date d'inscription du compte
	    @Column(nullable = false, updatable = false)
	    private LocalDateTime dateInscription = LocalDateTime.now();
}
