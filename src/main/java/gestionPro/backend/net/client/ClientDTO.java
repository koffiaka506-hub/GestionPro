package gestionPro.backend.net.client;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientDTO {
	
	private Long id;
    @NotBlank(message = "Le nom est obligatoire")
    private String nom;
    private String telephone;
    private String adresse;
    @Email(message = "Email invalide")
    private String email;

}
