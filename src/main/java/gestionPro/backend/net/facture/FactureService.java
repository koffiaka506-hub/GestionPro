package gestionPro.backend.net.facture;
 
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
 
import org.springframework.stereotype.Service;
 
import gestionPro.backend.net.client.Client;
import gestionPro.backend.net.client.ClientRepository;
import gestionPro.backend.net.ligneFacture.LigneFacture;
import gestionPro.backend.net.ligneFacture.LigneFactureDTO;
import gestionPro.backend.net.notification.WhatsAppService;
import gestionPro.backend.net.produit.Produit;
import gestionPro.backend.net.produit.ProduitRepository;
import gestionPro.backend.net.security.SecurityUtils;
import gestionPro.backend.net.utilisateur.Utilisateur;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
 
@Service
@RequiredArgsConstructor
public class FactureService {
 
    private final FactureRepository factureRepository;
    private final ClientRepository clientRepository;
    private final ProduitRepository produitRepository;
    private final WhatsAppService whatsAppService;
    private final SecurityUtils securityUtils;
 
    public List<FactureDTO> findAll() {
        Utilisateur u = securityUtils.getUtilisateurConnecte();
        return factureRepository.findByUtilisateurId(u.getId())
            .stream().map(this::toDTO).collect(Collectors.toList());
    }
 
    public FactureDTO findById(Long id) {
        Utilisateur u = securityUtils.getUtilisateurConnecte();
        Facture facture = factureRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Facture introuvable : id=" + id));
        if (!facture.getUtilisateur().getId().equals(u.getId())) {
            throw new RuntimeException("Accès refusé");
        }
        return toDTO(facture);
    }
 
    public List<FactureDTO> findByClient(Long clientId) {
        Utilisateur u = securityUtils.getUtilisateurConnecte();
        return factureRepository.findByClientIdAndUtilisateurId(clientId, u.getId())
            .stream().map(this::toDTO).collect(Collectors.toList());
    }
 
    @Transactional
    public FactureDTO create(FactureDTO dto) {
        Utilisateur u = securityUtils.getUtilisateurConnecte();
 
        Client client = clientRepository.findById(dto.getClientId())
            .orElseThrow(() -> new RuntimeException("Client introuvable"));
 
        Facture facture = Facture.builder()
            .client(client)
            .dateFacture(LocalDate.now())
            .utilisateur(u)
            .build();
 
        List<LigneFacture> lignes = dto.getLignes().stream().map(ligneDTO -> {
            Produit produit = produitRepository.findById(ligneDTO.getProduitId())
                .orElseThrow(() -> new RuntimeException("Produit introuvable : id=" + ligneDTO.getProduitId()));
 
            if (produit.getStock() < ligneDTO.getQuantite()) {
                throw new RuntimeException("Stock insuffisant pour : " + produit.getNom());
            }
 
            produit.setStock(produit.getStock() - ligneDTO.getQuantite());
            produitRepository.save(produit);
 
            return LigneFacture.builder()
                .facture(facture)
                .produit(produit)
                .quantite(ligneDTO.getQuantite())
                .prixUnitaire(produit.getPrix())
                .build();
        }).collect(Collectors.toList());
 
        facture.setLignes(lignes);
 
        List<String> details = lignes.stream().map(l ->
            "  • " + l.getProduit().getNom() +
            " x" + l.getQuantite() +
            " (stock restant: " + l.getProduit().getStock() + ")"
        ).collect(Collectors.toList());
 
        Facture saved = factureRepository.save(facture);
 
        double montantTotal = lignes.stream()
            .mapToDouble(l -> l.getPrixUnitaire() * l.getQuantite())
            .sum();
 
        whatsAppService.notifierNouvelleFacture(
            client.getNom(),
            montantTotal,
            lignes.size(),
            details
        );
 
        return toDTO(saved);
    }
 
    @Transactional
    public void delete(Long id) {
        Utilisateur u = securityUtils.getUtilisateurConnecte();
        Facture facture = factureRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Facture introuvable : id=" + id));
        if (!facture.getUtilisateur().getId().equals(u.getId())) {
            throw new RuntimeException("Accès refusé");
        }
        factureRepository.deleteById(id);
    }
 
    private FactureDTO toDTO(Facture f) {
        List<LigneFactureDTO> lignesDTO = f.getLignes().stream().map(l ->
            LigneFactureDTO.builder()
                .id(l.getId())
                .produitId(l.getProduit().getId())
                .produitNom(l.getProduit().getNom())
                .quantite(l.getQuantite())
                .prixUnitaire(l.getPrixUnitaire())
                .build()
        ).collect(Collectors.toList());
 
        return FactureDTO.builder()
            .id(f.getId())
            .clientId(f.getClient().getId())
            .clientNom(f.getClient().getNom())
            .dateFacture(f.getDateFacture())
            .montantTotal(f.getMontantTotal())
            .lignes(lignesDTO)
            .build();
    }
}