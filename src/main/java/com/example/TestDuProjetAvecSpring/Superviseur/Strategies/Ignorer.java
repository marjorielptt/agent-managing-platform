package com.example.TestDuProjetAvecSpring.Superviseur.Strategies;

import com.example.TestDuProjetAvecSpring.Superviseur.Superviseur;

public class Ignorer implements Superviseur {
    @Override
    public void executer(String microservice) {
        System.out.println("Ignorement du microservice : " + microservice);
        // Logique d'ignorement du microservice
    }
}
