package gestionPro.backend.net.client;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import gestionPro.backend.net.security.SecurityUtils;
import gestionPro.backend.net.utilisateur.Utilisateur;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClientService {
 
    private final ClientRepository clientRepository;
    private final SecurityUtils securityUtils;
 
    public List<ClientDTO> findAll() {
        Utilisateur u = securityUtils.getUtilisateurConnecte();
        return clientRepository.findByUtilisateurId(u.getId())
            .stream().map(this::toDTO).collect(Collectors.toList());
    }
 
    public ClientDTO findById(Long id) {
        Utilisateur u = securityUtils.getUtilisateurConnecte();
        Client client = clientRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Client introuvable"));
        if (!client.getUtilisateur().getId().equals(u.getId())) {
            throw new RuntimeException("Accès refusé");
        }
        return toDTO(client);
    }
 
    public ClientDTO create(ClientDTO dto) {
        Utilisateur u = securityUtils.getUtilisateurConnecte();
        Client client = Client.builder()
            .nom(dto.getNom())
            .telephone(dto.getTelephone())
            .adresse(dto.getAdresse())
            .email(dto.getEmail())
            .utilisateur(u) // ← lier à l'utilisateur connecté
            .build();
        return toDTO(clientRepository.save(client));
    }
 
    public ClientDTO update(Long id, ClientDTO dto) {
        Utilisateur u = securityUtils.getUtilisateurConnecte();
        Client client = clientRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Client introuvable"));
        if (!client.getUtilisateur().getId().equals(u.getId())) {
            throw new RuntimeException("Accès refusé");
        }
        client.setNom(dto.getNom());
        client.setTelephone(dto.getTelephone());
        client.setAdresse(dto.getAdresse());
        client.setEmail(dto.getEmail());
        return toDTO(clientRepository.save(client));
    }
 
    public void delete(Long id) {
        Utilisateur u = securityUtils.getUtilisateurConnecte();
        Client client = clientRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Client introuvable"));
        if (!client.getUtilisateur().getId().equals(u.getId())) {
            throw new RuntimeException("Accès refusé");
        }
        clientRepository.deleteById(id);
    }
 
    private ClientDTO toDTO(Client c) {
        return ClientDTO.builder()
            .id(c.getId())
            .nom(c.getNom())
            .telephone(c.getTelephone())
            .adresse(c.getAdresse())
            .email(c.getEmail())
            .build();
    }
}