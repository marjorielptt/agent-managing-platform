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
    public void onReceive(ActorMessage message) throws Exception{
        Thread.sleep(5000);
        System.out.println();
        System.out.println("• "+getId() + " a reçu : " + message.getPayload());
    }

    public void declarerProbleme(String typeProbleme) {
        String message = "";

        switch(typeProbleme) {
            case "COEUR":
                message = "J'ai une douleur thoracique intense et mal au bras gauche !";
                break;
            case "EPILEPSIE":
                message = "Je sens une crise d'épilepsie arriver, je suis seul.";
                break;
            case "JAMBE":
                message = "Je suis tombé, je crois que ma jambe est cassée.";
                break;
            default:
                message = "Je ne me sens pas bien.";
        }

        System.out.println("• 🚨 Patient " + getId() + " signale : " + typeProbleme);
        // On envoie toujours à la hotline
        ActorMessage msg = new ActorMessage(getId(), "hotline-1", "service-hotline", message);
        system.send(msg);
    }
}
