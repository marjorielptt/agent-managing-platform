package com.hopital;

import com.framework.Actor;
import com.framework.ActorMessage;
import com.framework.ActorSystem;
import com.framework.GestionLog;

public class MedecinActor extends Actor {
    private final ActorSystem system;

    public MedecinActor(String id, ActorSystem system) {
        super(id);
        this.system = system;
    }

    @Override
    public void onReceive(ActorMessage message) throws Exception {
        afficherSeparateurDossier();
        String dossier = message.getPayload().toString();
        System.out.println("• 👨‍⚕️ Médecin lit le dossier : " + dossier);
        GestionLog.ecrire("MEDECIN", "Réception et lecture du dossier : " + dossier);

        // Analyse du cas transmis par la Hotline
        if (dossier.startsWith("INFARCTUS")) {
            System.out.println("• \u001B[31m Médecin : C'est un arrêt cardiaque potentiel ! J'envoie le SMUR.\u001B[0m");
            GestionLog.ecrire("MEDECIN", "Décision : URGENCE VITALE -> Envoi équipe SMUR");
            ActorMessage ordre = new ActorMessage(getId(), "ambulancier-1", null, "SMUR - Départ Immédiat - " + dossier);
            system.send(ordre);

        } else if (dossier.startsWith("NEURO")) {
            System.out.println("• \u001B[33m Médecin : Risque de chute. J'envoie une ambulance standard.\u001B[0m");
            GestionLog.ecrire("MEDECIN", "Décision : Transport médicalisé requis -> Envoi Ambulance (VSAV)");
            ActorMessage ordre = new ActorMessage(getId(), "ambulancier-1", null, "VSAV - Transport calme - " + dossier);
            system.send(ordre);

        } else { // Cas par défaut
            System.out.println("• \u001B[32m Médecin : Pas d'action requise pour l'instant.\u001B[0m");
            GestionLog.ecrire("MEDECIN", "Décision : Pas d'intervention mobile nécessaire.");
        }
    }

    private void afficherSeparateurDossier() {
        System.out.println("\n\n\n\n\n\n\n\n");
        System.out.println("▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
        System.out.println("                          📂  NOUVEAU DOSSIER MÉDICAL OUVERT   -   " + java.time.LocalTime.now().withNano(0));
        System.out.println("▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
    }
}
