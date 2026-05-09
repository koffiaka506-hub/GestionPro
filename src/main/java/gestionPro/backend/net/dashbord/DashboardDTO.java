package gestionPro.backend.net.dashbord;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import gestionPro.backend.net.produit.ProduitDTO;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardDTO {
    private long totalProduits;
    private long totalClients;
    private long totalFactures;
    private double chiffreAffaires;
    private long produitsStockFaible;
    private List<ProduitDTO> alertesStock;
}