package com.example.TestDuProjetAvecSpring.MicroServiceHotline;

import com.example.TestDuProjetAvecSpring.*;
import com.example.TestDuProjetAvecSpring.MicroServiceHopital.PersonnelMedical;

public class Hotline extends Acteur {

    private final PersonnelMedical personnelMedical; // référence au personnel médical

    public Hotline(PersonnelMedical personnelMedical) {
        super();
        this.personnelMedical = personnelMedical;
    }

    @Override
    protected void onReceive(Message message) {
        System.out.println("[Hotline] Message reçu : " + message.getContenu());

        switch (message.getType()) {

            case APPEL_PATIENT:
                if (message.getContenu().contains("blague")) {
                    System.out.println("→ Fin de l'appel, mauvaise blague.");
                } else {
                    System.out.println("→ Transfert au personnel médical.");
                    ActorSystem.send(
                            personnelMedical.getId(),
                            new Message(TypeMessage.DEMANDE_AMBULANCE, // On choisit ici si on veut une DEMANDE_AMBULANCE ou DEMANDE_MEDICAL
                                    message.getContenu(),
                                    this.getId(),
                                    personnelMedical.getId())
                    );
                }
                break;

            default:
                System.out.println("Message inattendu reçu par la hotline.");
        }
    }
}