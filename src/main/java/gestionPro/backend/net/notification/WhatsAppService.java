package gestionPro.backend.net.notification;
 
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
 
@Service
@RequiredArgsConstructor
public class WhatsAppService {
 
    @Value("${ultramsg.instance}")
    private String instance;
 
    @Value("${ultramsg.token}")
    private String token;
 
    @Value("${ultramsg.patron}")
    private String patronPhone;
 
    private final RestTemplate restTemplate = new RestTemplate();
 
    // Envoyer un message WhatsApp
    public void sendMessage(String message) {
        try {
            String url = "https://api.ultramsg.com/" + instance + "/messages/chat";
 
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
 
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("token", token);
            params.add("to", patronPhone);
            params.add("body", message);
 
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
            restTemplate.postForEntity(url, request, String.class);
 
        } catch (Exception e) {
            // On log l'erreur mais on n'arrête pas l'application
            System.err.println("Erreur WhatsApp : " + e.getMessage());
        }
    }
 
    // Message après création d'une facture
    public void notifierNouvelleFacture(String clientNom, double montant, int nbProduits, List<String> details) {
        String lignesDetail = String.join("\n", details);
        
        String message = "🧾 *Nouvelle Facture — GestionPro*\n\n" +
            "👤 Client : " + clientNom + "\n" +
            "📦 Produits vendus :\n" + lignesDetail + "\n\n" +
            "💰 Montant : " + String.format("%,.0f", montant) + " FCFA\n\n" +
            "🕐 " + java.time.LocalDateTime.now()
                .format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        sendMessage(message);
    }
 
    // Alerte stock faible
    public void notifierStockFaible(String produitNom, int stockRestant) {
        String message = "⚠️ *Alerte Stock — GestionPro*\n\n" +
            "📦 Produit : " + produitNom + "\n" +
            "🔴 Stock restant : " + stockRestant + " unité(s)\n\n" +
            "Pensez à réapprovisionner !";
        sendMessage(message);
    }
}