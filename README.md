# Framework multi-agents

Framework d’acteurs distribués, où chaque **acteur** représente une entité capable de **recevoir et traiter des messages**. Les acteurs peuvent également communiquer entre eux, qu'ils soient situés dans le même **microservice** ou non.
Notre framework a les propriétés suivantes :
- **Résilience** : Il gère les pannes d'acteurs sans avoir à redémarrer l'application,
- **Élasticité** : Il s'adapte dynamiquement à charge en créant ou supprimant des instances d'acteurs.

Ce projet contient également un exemple d'application du framework : le SAMU. 

Ce projet est inspiré du framework [Akka](https://github.com/akka/akka-core).

## Tutoriel d'utilisation du framework

Vous trouverez le fichier jar à installer dans 

## Tutoriel d'utilisation de l'exemple d'application du SAMU

### Configuration

- Télécharger le code source du projet et l'ouvrir dans l'IDE de votre choix.

- Ouvrir un premier terminal et exécuter le fichier `discovery-server/src/main/java/com.discovery/DiscoveryApplication.java`
- Ouvrir un deuxième terminal et exécuter le fichier `service-hopital/src/main/java/com.patient/HopitalApp.java`
- Ouvrir un troisième terminal et exécuter le fichier `service-hotline/src/main/java/com.hotline/HotlineApp.java`
- Ouvrir un quatrième terminal et exécuter le fichier `service-patient/src/main/java/com.patient/PatientApp.java`

### Prise en main

- Dans le terminal du patient, saisir dans la console le numéro de la maladie dont souffre le patient, puis patientez. Vous allez être pris en charge ! 🚑

![Console display](images/menuPatient.png)
