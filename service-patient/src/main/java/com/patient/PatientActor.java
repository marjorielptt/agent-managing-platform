package com.patient;

import com.framework.Actor;
import com.framework.ActorMessage;
import com.framework.ActorSystem;

public class PatientActor extends Actor {
    private final ActorSystem system;

    public PatientActor(String id, ActorSystem system) {
        super(id);
        this.system = system;
    }

    @Override
    public void onReceive(ActorMessage message) {
        System.out.println("🤕 Patient " + getId() + " a reçu : " + message.getPayload());
    }

    public void avoirMalaise() {
        System.out.println("🚨 Patient " + getId() + " appelle la hotline !");
        ActorMessage msg = new ActorMessage(getId(), "hotline-1", "service-hotline", "J'ai mal au coeur !");
        system.send(msg);
    }
}