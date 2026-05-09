package gestionPro.backend.net.dashbord;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import gestionPro.backend.net.client.ClientRepository;
import gestionPro.backend.net.facture.FactureRepository;
import gestionPro.backend.net.produit.ProduitDTO;
import gestionPro.backend.net.produit.ProduitRepository;
import gestionPro.backend.net.retour.RetourRepository;
import gestionPro.backend.net.security.SecurityUtils;
import gestionPro.backend.net.utilisateur.Utilisateur;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DashboardService {
 
    private final ProduitRepository produitRepository;
    private final ClientRepository clientRepository;
    private final FactureRepository factureRepository;
    private final RetourRepository retourRepository;
    private final SecurityUtils securityUtils;
 
    public DashboardDTO getStats() {
        Utilisateur u = securityUtils.getUtilisateurConnecte();
        Long uid = u.getId();
 
        List<ProduitDTO> alertes = produitRepository.findStockFaibleByUtilisateur(uid)
            .stream().map(p -> ProduitDTO.builder()
                .id(p.getId()).nom(p.getNom())
                .categorie(p.getCategorie())
                .prix(p.getPrix()).stock(p.getStock())
                .build())
            .collect(Collectors.toList());
 
        Double ca = factureRepository.calculerChiffreAffaires(uid);
 
        return DashboardDTO.builder()
            .totalProduits(produitRepository.findByUtilisateurId(uid).size())
            .totalClients(clientRepository.findByUtilisateurId(uid).size())
            .totalFactures(factureRepository.findByUtilisateurId(uid).size())
            .chiffreAffaires(ca != null ? ca : 0.0)
            .produitsStockFaible(alertes.size())
            .alertesStock(alertes)
            .build();
    }
}