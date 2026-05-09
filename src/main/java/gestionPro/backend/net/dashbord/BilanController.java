package gestionPro.backend.net.dashbord;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import gestionPro.backend.net.facture.FactureRepository;
import gestionPro.backend.net.retour.RetourRepository;
import gestionPro.backend.net.security.SecurityUtils;
import gestionPro.backend.net.utilisateur.Utilisateur;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/bilan")
@RequiredArgsConstructor
public class BilanController {
 
    private final FactureRepository factureRepository;
    private final RetourRepository retourRepository;
    private final SecurityUtils securityUtils;
 
    // GET /api/bilan/journalier
    @GetMapping("/journalier")
    public ResponseEntity<Map<String, Object>> bilanJournalier() {
        Utilisateur u = securityUtils.getUtilisateurConnecte();
        Long uid = u.getId();
 
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);
 
        Double caAujourdhui = factureRepository.calculerCAEntre(uid, today, today);
        Double caHier = factureRepository.calculerCAEntre(uid, yesterday, yesterday);
        Long facturesAujourdhui = factureRepository.countByDate(uid, today);
        Long retoursAujourdhui = retourRepository.countByDate(uid, today);
        Double rembourseAujourdhui = retourRepository.totalRembourse(uid, today, today);
 
        Map<String, Object> bilan = new HashMap<>();
        bilan.put("date", today);
        bilan.put("chiffreAffaires", caAujourdhui != null ? caAujourdhui : 0.0);
        bilan.put("chiffreAffairesHier", caHier != null ? caHier : 0.0);
        bilan.put("nombreFactures", facturesAujourdhui);
        bilan.put("nombreRetours", retoursAujourdhui);
        bilan.put("montantRembourse", rembourseAujourdhui != null ? rembourseAujourdhui : 0.0);
        bilan.put("beneficeNet", (caAujourdhui != null ? caAujourdhui : 0.0)
                               - (rembourseAujourdhui != null ? rembourseAujourdhui : 0.0));
 
        return ResponseEntity.ok(bilan);
    }
 
    // GET /api/bilan/mensuel?annee=2026&mois=5
    @GetMapping("/mensuel")
    public ResponseEntity<Map<String, Object>> bilanMensuel(
            @RequestParam(defaultValue = "0") int annee,
            @RequestParam(defaultValue = "0") int mois) {
 
        Utilisateur u = securityUtils.getUtilisateurConnecte();
        Long uid = u.getId();
 
        YearMonth ym = (annee == 0 || mois == 0)
            ? YearMonth.now()
            : YearMonth.of(annee, mois);
 
        LocalDate debut = ym.atDay(1);
        LocalDate fin = ym.atEndOfMonth();
 
        YearMonth ymPrecedent = ym.minusMonths(1);
        LocalDate debutPrec = ymPrecedent.atDay(1);
        LocalDate finPrec = ymPrecedent.atEndOfMonth();
 
        Double caMois = factureRepository.calculerCAEntre(uid, debut, fin);
        Double caMoisPrec = factureRepository.calculerCAEntre(uid, debutPrec, finPrec);
        Long facturesMois = factureRepository.countBetween(uid, debut, fin);
        Double rembourse = retourRepository.totalRembourse(uid, debut, fin);
 
        double evolution = 0;
        if (caMoisPrec != null && caMoisPrec > 0 && caMois != null) {
            evolution = ((caMois - caMoisPrec) / caMoisPrec) * 100;
        }
 
        Map<String, Object> bilan = new HashMap<>();
        bilan.put("mois", ym.toString());
        bilan.put("chiffreAffaires", caMois != null ? caMois : 0.0);
        bilan.put("chiffreAffairesMoisPrecedent", caMoisPrec != null ? caMoisPrec : 0.0);
        bilan.put("evolutionPourcentage", Math.round(evolution * 10.0) / 10.0);
        bilan.put("nombreFactures", facturesMois);
        bilan.put("montantRembourse", rembourse != null ? rembourse : 0.0);
        bilan.put("beneficeNet", (caMois != null ? caMois : 0.0)
                               - (rembourse != null ? rembourse : 0.0));
 
        return ResponseEntity.ok(bilan);
    }
}
