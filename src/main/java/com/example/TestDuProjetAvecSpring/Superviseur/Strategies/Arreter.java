package com.example.TestDuProjetAvecSpring.Superviseur.Strategies;

import com.example.TestDuProjetAvecSpring.Superviseur.Superviseur;

public class Arreter implements Superviseur {
    @Override
    public void executer(String microservice) {
        System.out.println("Arrêt du microservice : " + microservice);
        // Logique d'arrêt du microservice
    }
    
}
