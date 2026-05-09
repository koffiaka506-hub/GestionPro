package gestionPro.backend.net.retour;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import gestionPro.backend.net.client.Client;
import gestionPro.backend.net.client.ClientRepository;
import gestionPro.backend.net.produit.Produit;
import gestionPro.backend.net.produit.ProduitRepository;
import gestionPro.backend.net.security.SecurityUtils;
import gestionPro.backend.net.utilisateur.Utilisateur;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RetourService {
 
    private final RetourRepository retourRepository;
    private final ClientRepository clientRepository;
    private final ProduitRepository produitRepository;
    private final SecurityUtils securityUtils;
 
    public List<RetourDTO> findAll() {
        Utilisateur u = securityUtils.getUtilisateurConnecte();
        return retourRepository.findByUtilisateurId(u.getId())
            .stream().map(this::toDTO).collect(Collectors.toList());
    }
 
    public RetourDTO findById(Long id) {
        Utilisateur u = securityUtils.getUtilisateurConnecte();
        Retour retour = retourRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Retour introuvable : id=" + id));
        if (!retour.getUtilisateur().getId().equals(u.getId())) {
            throw new RuntimeException("Accès refusé");
        }
        return toDTO(retour);
    }
 
    public List<RetourDTO> findByClient(Long clientId) {
        Utilisateur u = securityUtils.getUtilisateurConnecte();
        return retourRepository.findByClientIdAndUtilisateurId(clientId, u.getId())
            .stream().map(this::toDTO).collect(Collectors.toList());
    }
 
    @Transactional
    public RetourDTO create(RetourDTO dto) {
        Utilisateur u = securityUtils.getUtilisateurConnecte();
 
        Client client = clientRepository.findById(dto.getClientId())
            .orElseThrow(() -> new RuntimeException("Client introuvable"));
 
        Produit produit = produitRepository.findById(dto.getProduitId())
            .orElseThrow(() -> new RuntimeException("Produit introuvable"));
 
        // Réincrémenter le stock
        produit.setStock(produit.getStock() + dto.getQuantite());
        produitRepository.save(produit);
 
        double montant = produit.getPrix() * dto.getQuantite();
 
        Retour retour = Retour.builder()
            .client(client)
            .produit(produit)
            .quantite(dto.getQuantite())
            .motif(dto.getMotif())
            .description(dto.getDescription())
            .montantRembourse(montant)
            .utilisateur(u)
            .build();
 
        return toDTO(retourRepository.save(retour));
    }
 
    @Transactional
    public void delete(Long id) {
        Utilisateur u = securityUtils.getUtilisateurConnecte();
        Retour retour = retourRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Retour introuvable : id=" + id));
        if (!retour.getUtilisateur().getId().equals(u.getId())) {
            throw new RuntimeException("Accès refusé");
        }
        Produit produit = retour.getProduit();
        produit.setStock(produit.getStock() - retour.getQuantite());
        produitRepository.save(produit);
        retourRepository.deleteById(id);
    }
 
    private RetourDTO toDTO(Retour r) {
        return RetourDTO.builder()
            .id(r.getId())
            .clientId(r.getClient().getId())
            .clientNom(r.getClient().getNom())
            .produitId(r.getProduit().getId())
            .produitNom(r.getProduit().getNom())
            .quantite(r.getQuantite())
            .motif(r.getMotif())
            .description(r.getDescription())
            .dateRetour(r.getDateRetour())
            .montantRembourse(r.getMontantRembourse())
            .build();
    }
}