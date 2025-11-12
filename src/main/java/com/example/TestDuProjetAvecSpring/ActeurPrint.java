package com.example.TestDuProjetAvecSpring;

public class ActeurPrint extends Acteur {

    @Override
    protected void onReceive(Message message) {
        System.out.println("Acteur " + getId() +
                " a reçu : " + message.getContenu());
    }
}