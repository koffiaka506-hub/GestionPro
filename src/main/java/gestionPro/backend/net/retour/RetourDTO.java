package gestionPro.backend.net.retour;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
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
public class RetourDTO {
 
    private Long id;
 
    @NotNull(message = "L'ID du client est obligatoire")
    private Long clientId;
    private String clientNom;      // lecture seulement
 
    @NotNull(message = "L'ID du produit est obligatoire")
    private Long produitId;
    private String produitNom;     // lecture seulement
 
    @NotNull
    @Positive(message = "La quantité doit être positive")
    private Integer quantite;
 
    @NotBlank(message = "Le motif est obligatoire")
    private String motif;
 
    private String description;
    private LocalDate dateRetour;
    private Double montantRembourse;
}