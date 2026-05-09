package gestionPro.backend.net.facture;

import java.time.LocalDate;
import java.util.List;

import gestionPro.backend.net.ligneFacture.LigneFactureDTO;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FactureDTO {
	
	 private Long id;
	 
	    @NotNull(message = "L'ID du client est obligatoire")
	    private Long clientId;
	 
	    private String clientNom;     // lecture seulement
	    private LocalDate dateFacture;
	    private Double montantTotal;  // calculé automatiquement
	 
	    @NotNull
	    @Size(min = 1, message = "La facture doit avoir au moins une ligne")
	    private List<LigneFactureDTO> lignes;

}
