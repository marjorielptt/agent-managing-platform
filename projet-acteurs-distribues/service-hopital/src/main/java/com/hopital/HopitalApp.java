package com.hopital;

import com.framework.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.hopital", "com.framework"})
public class HopitalApp {
    public static void main(String[] args) {
        SpringApplication.run(HopitalApp.class, args);
    }

    @Bean
    public CommandLineRunner init(ActorSystem system) {
        return args -> {
            system.registerActor(new MedecinActor("medecin-urgences", system));
            system.registerActor(new AmbulancierActor("ambulancier-1"));
        };
    }
}