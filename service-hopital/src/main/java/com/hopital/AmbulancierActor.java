package com.hopital;

import com.framework.Actor;
import com.framework.ActorMessage;

public class AmbulancierActor extends Actor {
    public AmbulancierActor(String id) {
        super(id);
    }

    @Override
    public void onReceive(ActorMessage message) {
        System.out.println("🚐 Ambulancier en route : " + message.getPayload());
    }
}