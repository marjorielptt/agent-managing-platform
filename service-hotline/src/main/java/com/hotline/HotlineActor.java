package com.hotline;

import com.framework.Actor;
import com.framework.ActorMessage;
import com.framework.ActorSystem;
import com.framework.GestionLog;

public class HotlineActor extends Actor {
    private final ActorSystem system;

    public HotlineActor(String id, ActorSystem system) {
        super(id);
        this.system = system;
    }

    @Override
    public void onReceive(ActorMessage message) {

        afficherSeparateurSimu();
        String texte = message.getPayload().toString().toLowerCase(); // Tout en minuscule pour faciliter
        String sender = message.getSenderId();

        System.out.println("• Hotline analyse l'appel de " + sender);
        GestionLog.ecrire("HOTLINE", "Appel reçu de " + sender + " contenant : \"" + texte + "\"");

        if (texte.contains("thoracique") || texte.contains("bras gauche")) {
            // CAS 1 : C'EST GRAVE -> On envoie au Médecin Urgentiste avec priorité
            System.out.println("• \u001B[31m CODE ROUGE : Suspections infarctus !\u001B[0m");
            GestionLog.ecrire("HOTLINE", "Analyse : CODE ROUGE (Infarctus) -> Transfert immédiat au Médecin");
            ActorMessage urgence = new ActorMessage(getId(), "medecin-urgences", "service-hopital", "INFARCTUS suspecté pour " + sender);
            system.send(urgence);

        } else if (texte.contains("épilepsie")) {
            // CAS 2 : NEURO -> On pourrait avoir un médecin spécialisé
            System.out.println("• \u001B[33m CODE ORANGE : Crise convulsive.\u001B[0m");
            GestionLog.ecrire("HOTLINE", "Analyse : CODE ORANGE (Neuro) -> Transfert au Médecin");
            ActorMessage neuro = new ActorMessage(getId(), "medecin-urgences", "service-hopital", "NEURO : Crise épilepsie pour " + sender);
            system.send(neuro);

        } else if (texte.contains("jambe") || texte.contains("cassée")) {
            // CAS 3 : TRAUMA -> Pas besoin de réveiller le médecin urgentiste, on conseille juste
            System.out.println("• \u001B[32m CODE VERT : Trauma membre inférieur.\u001B[0m");
            GestionLog.ecrire("HOTLINE", "Analyse : CODE VERT (Bobologie) -> Conseil direct au patient (Pas de médecin)");
            try { Thread.sleep(2000); } catch (InterruptedException e) {}

            // Hotline répond directement au lieu de transférer
            ActorMessage reponse = new ActorMessage(getId(), sender, "service-patient", "Ne bougez pas, mettez de la glace, on vous envoie un taxi conventionné.");
            system.send(reponse);

        } else {
            System.out.println("• Incompris, demande de précision.");
            GestionLog.ecrire("HOTLINE", "Analyse : Message incompris, demande de précisions.");
        }
    }

    private void afficherSeparateurSimu() {
        System.out.println("\n\n\n\n\n\n\n\n");
        System.out.println("▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
        System.out.println("                                 NOUVELLE ALERTE REÇUE  -  " + java.time.LocalTime.now().withNano(0));
        System.out.println("▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
    }
}
