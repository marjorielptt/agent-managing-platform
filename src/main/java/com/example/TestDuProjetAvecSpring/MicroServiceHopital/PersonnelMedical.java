package com.example.TestDuProjetAvecSpring.MicroServiceHopital;

import com.example.TestDuProjetAvecSpring.Acteur;
import com.example.TestDuProjetAvecSpring.ActorSystem;
import com.example.TestDuProjetAvecSpring.Message;
import com.example.TestDuProjetAvecSpring.TypeMessage;

public class PersonnelMedical extends Acteur {

    private final Medecin medecin;       // référence au médecin
    private final Ambulancier ambulancier; // référence à l'ambulancier

    public PersonnelMedical(Medecin medecin, Ambulancier ambulancier) {
        super();
        this.medecin = medecin;
        this.ambulancier = ambulancier;
    }

    @Override
    protected void onReceive(Message message) {
        System.out.println("[Personnel Médical] Message reçu : " + message.getContenu());

        switch (message.getType()) {

            case DEMANDE_MEDICAL:
                System.out.println("→ Appel d'un médecin");
                ActorSystem.send(
                        medecin.getId(),
                        new Message(TypeMessage.DEMANDE_MEDICAL,
                                message.getContenu(),
                                this.getId(),
                                medecin.getId())
                );
                break;

            case DEMANDE_AMBULANCE:
                System.out.println("→ Appel d'un ambulancier");
                ActorSystem.send(
                        ambulancier.getId(),
                        new Message(TypeMessage.DEMANDE_AMBULANCE,
                                message.getContenu(),
                                this.getId(),
                                ambulancier.getId())
                );
                break;

            default:
                System.out.println("Type de message inattendu!");
        }
    }
}
