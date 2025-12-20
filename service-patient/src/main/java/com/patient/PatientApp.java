package com.patient;

import com.framework.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

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

            System.out.println("⏳ Patient Bob attend un peu avant d'avoir un malaise...");
            Thread.sleep(5000); // Attendre que tout le monde soit up
            
            bob.avoirMalaise();
        };
    }
}