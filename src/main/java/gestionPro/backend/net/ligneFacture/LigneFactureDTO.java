package gestionPro.backend.net.ligneFacture;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LigneFactureDTO {
	
	 private Long id;
	 
	    @NotNull(message = "L'ID du produit est obligatoire")
	    private Long produitId;
	 
	    private String produitNom;    // lecture seulement (retourné par l'API)
	    private Double prixUnitaire;  // lecture seulement
	 
	    @NotNull
	    @Positive(message = "La quantité doit être positive")
	    private Integer quantite;

}
