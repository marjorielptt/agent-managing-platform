package com.hotline;

import com.framework.Actor;
import com.framework.ActorMessage;
import com.framework.ActorSystem;

public class HotlineActor extends Actor {
    private final ActorSystem system;

    public HotlineActor(String id, ActorSystem system) {
        super(id);
        this.system = system;
    }

    @Override
    public void onReceive(ActorMessage message) {
        System.out.println("📞 Hotline reçoit appel de " + message.getSenderId() + ": " + message.getPayload());
        
        if (message.getPayload().toString().contains("coeur")) {
            System.out.println("⚡️ URGENCE DÉTECTÉE -> Transfert Hôpital");
            
            ActorMessage forward = new ActorMessage(getId(), "medecin-urgences", "service-hopital", "Urgence cardiaque pour " + message.getSenderId());
            system.send(forward);
        }
    }
}