package gestionPro.backend.net.ligneFacture;

import gestionPro.backend.net.facture.Facture;
import gestionPro.backend.net.produit.Produit;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Table(name = "lignes_facture")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LigneFacture {
	

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
 
    // Relation vers la facture parente
    @ManyToOne
    @JoinColumn(name = "facture_id", nullable = false)
    private Facture facture;
 
    // Relation vers le produit
    @ManyToOne
    @JoinColumn(name = "produit_id", nullable = false)
    private Produit produit;
 
    @Column(nullable = false)
    private Integer quantite;
 
    // Prix au moment de la vente (peut différer du prix actuel)
    @Column(nullable = false)
    private Double prixUnitaire;

}
