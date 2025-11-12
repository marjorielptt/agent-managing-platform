package com.example.TestDuProjetAvecSpring.Superviseur.Strategies;

import com.example.TestDuProjetAvecSpring.Superviseur.Superviseur;

public class Redemarrer implements Superviseur {
    @Override
    public void executer(String microservice) {
        System.out.println("Redémarrage du microservice : " + microservice);
        // Logique de redémarrage du microservice
    }
    
}
