package com.example.TestDuProjetAvecSpring.MicroServiceHopital;

import com.example.TestDuProjetAvecSpring.Acteur;
import com.example.TestDuProjetAvecSpring.Message;

public class Ambulancier extends Acteur {
    @Override
    protected void onReceive(Message message) {
        System.out.println("Ambulancier se déplace pour: " + message.getContenu());
    }
}
