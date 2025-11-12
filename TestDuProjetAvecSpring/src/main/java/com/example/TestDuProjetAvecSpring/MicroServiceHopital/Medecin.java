package com.example.TestDuProjetAvecSpring.MicroServiceHopital;

import com.example.TestDuProjetAvecSpring.Acteur;
import com.example.TestDuProjetAvecSpring.Message;

public class Medecin extends Acteur {
    @Override
    protected void onReceive(Message message) {
        System.out.println("Médecin traite: " + message.getContenu());
    }
}