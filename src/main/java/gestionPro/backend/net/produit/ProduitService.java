package gestionPro.backend.net.produit;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import gestionPro.backend.net.security.SecurityUtils;
import gestionPro.backend.net.utilisateur.Utilisateur;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class ProduitService {

   private final ProduitRepository produitRepository;
   private final SecurityUtils securityUtils;

   public List<ProduitDTO> findAll() {
       Utilisateur u = securityUtils.getUtilisateurConnecte();
       return produitRepository.findByUtilisateurId(u.getId())
           .stream().map(this::toDTO).collect(Collectors.toList());
   }

   public ProduitDTO findById(Long id) {
       Utilisateur u = securityUtils.getUtilisateurConnecte();
       Produit produit = produitRepository.findById(id)
           .orElseThrow(() -> new RuntimeException("Produit introuvable"));
       // Vérifier que le produit appartient à l'utilisateur
       if (!produit.getUtilisateur().getId().equals(u.getId())) {
           throw new RuntimeException("Accès refusé");
       }
       return toDTO(produit);
   }

   public ProduitDTO create(ProduitDTO dto) {
       Utilisateur u = securityUtils.getUtilisateurConnecte();
       Produit produit = Produit.builder()
           .nom(dto.getNom())
           .categorie(dto.getCategorie())
           .prix(dto.getPrix())
           .stock(dto.getStock())
           .utilisateur(u) // ← lier à l'utilisateur connecté
           .build();
       return toDTO(produitRepository.save(produit));
   }

   public ProduitDTO update(Long id, ProduitDTO dto) {
       Utilisateur u = securityUtils.getUtilisateurConnecte();
       Produit existant = produitRepository.findById(id)
           .orElseThrow(() -> new RuntimeException("Produit introuvable"));
       if (!existant.getUtilisateur().getId().equals(u.getId())) {
           throw new RuntimeException("Accès refusé");
       }
       existant.setNom(dto.getNom());
       existant.setCategorie(dto.getCategorie());
       existant.setPrix(dto.getPrix());
       existant.setStock(dto.getStock());
       return toDTO(produitRepository.save(existant));
   }

   public void delete(Long id) {
       Utilisateur u = securityUtils.getUtilisateurConnecte();
       Produit existant = produitRepository.findById(id)
           .orElseThrow(() -> new RuntimeException("Produit introuvable"));
       if (!existant.getUtilisateur().getId().equals(u.getId())) {
           throw new RuntimeException("Accès refusé");
       }
       produitRepository.deleteById(id);
   }

   public List<ProduitDTO> findStockFaible() {
       Utilisateur u = securityUtils.getUtilisateurConnecte();
       return produitRepository.findStockFaibleByUtilisateur(u.getId())
           .stream().map(this::toDTO).collect(Collectors.toList());
   }

   private ProduitDTO toDTO(Produit p) {
       return ProduitDTO.builder()
           .id(p.getId())
           .nom(p.getNom())
           .categorie(p.getCategorie())
           .prix(p.getPrix())
           .stock(p.getStock())
           .build();
   }
}