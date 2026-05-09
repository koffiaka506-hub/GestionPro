package gestionPro.backend.net.produit;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProduitDTO {
	
	private Long id;                          // null lors de la création
	 
    @NotBlank(message = "Le nom est obligatoire")
    private String nom;
 
    private String categorie;
 
    @NotNull(message = "Le prix est obligatoire")
    @Positive(message = "Le prix doit être positif")
    private Double prix;
 
    @NotNull(message = "Le stock est obligatoire")
    @PositiveOrZero
    private Integer stock;

}
