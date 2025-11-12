package com.example.TestDuProjetAvecSpring;

import com.example.TestDuProjetAvecSpring.MicroServiceHopital.Ambulancier;
import com.example.TestDuProjetAvecSpring.MicroServiceHopital.Medecin;
import com.example.TestDuProjetAvecSpring.MicroServiceHopital.PersonnelMedical;
import com.example.TestDuProjetAvecSpring.MicroServiceHotline.Hotline;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TestDuProjetAvecSpringApplication {

	public static void main(String[] args) {

		SpringApplication.run(TestDuProjetAvecSpringApplication.class, args);


		Medecin medecin = new Medecin();
		Ambulancier ambulance = new Ambulancier();

		PersonnelMedical personnel = new PersonnelMedical(medecin, ambulance);
		Hotline hotline = new Hotline(personnel);

		ActorSystem.register(hotline);
		ActorSystem.register(personnel);
		ActorSystem.register(medecin);
		ActorSystem.register(ambulance);

		// ID de l’acteur pour le routage
		hotline.envoyer(new Message(TypeMessage.APPEL_PATIENT, "Patient avec problème urgent", 0, hotline.getId()));
	}

}
