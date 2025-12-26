package com.hopital;

import com.framework.Actor;
import com.framework.ActorMessage;
import com.framework.ActorSystem;
import com.framework.GestionLog;

public class AmbulancierActor extends Actor {

    private final ActorSystem system;

    public AmbulancierActor(String id, ActorSystem system) {
        super(id);
        this.system = system;
    }

    @Override
    public void onReceive(ActorMessage message) throws Exception {
        if ("CRASH".equals(message.getPayload())) {
            throw new RuntimeException("• 🚨 Accident simulé ! L'ambulancier est hors service.");
        }
        String ordre = message.getPayload().toString();
        System.out.println("• 🚐 Ambulancier en route : " + ordre);
        GestionLog.ecrire("AMBULANCE", "Départ en intervention suite ordre : " + ordre);

        if (!"system".equals(message.getSenderId())) {

            String patientId = extractPatientId(message.getPayload().toString());

            ActorMessage response = new ActorMessage(getId(), patientId, "service-patient", "🚑 Tenez bon, j'arrive !");

            system.send(response);
        }
    }

    private String extractPatientId(String payload) {
        return "patient-bob";
    }
}
