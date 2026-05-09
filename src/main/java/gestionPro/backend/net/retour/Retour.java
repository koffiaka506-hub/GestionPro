package gestionPro.backend.net.retour;

import java.time.LocalDate;

import gestionPro.backend.net.client.Client;
import gestionPro.backend.net.produit.Produit;
import gestionPro.backend.net.utilisateur.Utilisateur;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "retours")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Retour {
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
 
    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;
 
    @ManyToOne
    @JoinColumn(name = "produit_id", nullable = false)
    private Produit produit;
 
    @NotNull
    @Positive
    @Column(nullable = false)
    private Integer quantite;
 
    @NotBlank
    @Column(nullable = false)
    private String motif;
 
    private String description;
 
    @Column(nullable = false)
    private LocalDate dateRetour;
 
    private Double montantRembourse;
 
    // Relation vers l'utilisateur propriétaire
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "utilisateur_id", nullable = false)
    private Utilisateur utilisateur;
 
    @PrePersist
    public void beforeSave() {
        this.dateRetour = LocalDate.now();
    }
}