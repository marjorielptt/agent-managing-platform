package com.hopital;

import com.framework.Actor;
import com.framework.ActorMessage;
import com.framework.ActorSystem;

public class MedecinActor extends Actor {
    private final ActorSystem system;

    public MedecinActor(String id, ActorSystem system) {
        super(id);
        this.system = system;
    }

    @Override
    public void onReceive(ActorMessage message) throws Exception {
        System.out.println("👨‍⚕️ Médecin reçoit le cas : " + message.getPayload());
        
        // Simulation de panne aléatoire pour tester la supervision
        if (Math.random() > 0.8) {
            throw new RuntimeException("Le médecin a glissé sur une peau de banane !");
        }

        // Envoi local à l'ambulancier
        System.out.println("🚑 Médecin déclenche l'ambulance (local)");
        ActorMessage ordre = new ActorMessage(getId(), "ambulancier-1", null, "Go chercher " + message.getPayload());
        system.send(ordre);
    }
}