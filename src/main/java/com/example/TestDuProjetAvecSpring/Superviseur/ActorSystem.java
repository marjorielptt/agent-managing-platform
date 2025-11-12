package com.example.TestDuProjetAvecSpring;

import java.util.HashMap;
import java.util.Map;

public class ActorSystem {
    private static Map<Integer, Acteur> acteurs = new HashMap<>();

    public static void register(Acteur acteur) {
        acteurs.put(acteur.getId(), acteur);
    }

    public static void send(int recepteurId, Message message) {
        Acteur a = acteurs.get(recepteurId);
        if (a != null) {
            a.envoyer(message);
        }
    }
}
