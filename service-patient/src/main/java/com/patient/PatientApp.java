package com.patient;

import com.framework.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import java.util.Scanner;

@SpringBootApplication
@ComponentScan(basePackages = {"com.patient", "com.framework"})
public class PatientApp {
    public static void main(String[] args) {
        SpringApplication.run(PatientApp.class, args);
    }

    @Bean
    public CommandLineRunner startScenario(ActorSystem system) {
        return args -> {

            PatientActor bob = new PatientActor("patient-bob", system);
            system.registerActor(bob);

            clearConsole();

            System.out.println("\n⏳ Synchronisation initiale du réseau en cours...");

            // Barre de chargement
            System.out.print("Progression : [");
            int totalSec = 20;
            for (int i = 0; i <= totalSec; i++) {
                String rempli = "";
                for (int j = 0; j < i; j++) { rempli += "#"; }

                String vide = "";
                for (int j = 0; j < (totalSec - i); j++) { vide += " "; }

                System.out.print("\rProgression : [" + rempli + vide + "] " + i + "/" + totalSec + "s");

                if (i < totalSec) {
                    Thread.sleep(1000);
                }
            }

            System.out.println("]\n");
            Thread.sleep(1000);

            Scanner scanner = new Scanner(System.in);
            boolean continuer = true;

            GestionLog.sautDeSection();

            while (continuer) {
                clearConsole();
                System.out.println("\n▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
                System.out.println("                                           SIMULATEUR SAMU");
                System.out.println("▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
                System.out.println("Choisissez le scénario à lancer :");
                System.out.println("  1 -> \u001B[31mCœur\u001B[0m               : J'ai une douleur thoracique intense et mal au bras gauche !");
                System.out.println("  2 -> \u001B[33mCrise d'épilepsie\u001B[0m  : Je sens une crise d'épilepsie arriver, je suis seul.");
                System.out.println("  3 -> \u001B[32mJambe cassée\u001B[0m       : Je suis tombé, je crois que ma jambe est cassée.");
                System.out.println("  0 -> Arret de la Simulation.");
                System.out.println("\n▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
                System.out.print("Votre choix (0-3) : ");


                String input = scanner.nextLine();
                int choix = -1;
                try {
                    choix = Integer.parseInt(input);
                } catch (NumberFormatException e) {
                    choix = -1; // Meme si choix est une chaine de caractère, on va dans le cas par défaut sans crash
                }

                System.out.println();

                switch (choix) {
                    case 0:
                        continuer = false;
                        break;
                    case 1:
                        bob.declarerProbleme("COEUR");
                        break;
                    case 2:
                        bob.declarerProbleme("EPILEPSIE");
                        break;
                    case 3:
                        bob.declarerProbleme("JAMBE");
                        break;
                    default:
                        System.out.println("❌ Choix invalide, "+bob.getId()+" ne fait rien.");
                }
                if (!continuer) {
                    break;
                }
                Thread.sleep(8000);

                System.out.println("\n▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
                boolean reponseValide = false;
                while (!reponseValide) {
                    System.out.print("Voulez-vous lancer un autre scénario ? (o/n) : ");
                    String reponse = scanner.nextLine();

                    if (reponse.equalsIgnoreCase("o")) {
                        continuer = true;
                        reponseValide = true;
                    } else if (reponse.equalsIgnoreCase("n")) {
                        continuer = false;
                        reponseValide = true;
                    } else {
                        System.out.println("❌ Réponse non reconnue. Tapez juste 'o' ou 'n'.");
                    }
                }
            }
            System.out.println("Fin de la simulation. Arrêt du système.");
            System.exit(0);
        };
    }

    private void clearConsole() {
        for(int i = 0; i < 50; i++) {
            System.out.println();
        }
    }

}

