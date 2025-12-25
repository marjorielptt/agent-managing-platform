package com.framework;

import lombok.Getter;

public abstract class Actor {
    @Getter
    private final String id;
    @Getter
    private int failureCount = 0;
    @Getter
    private Supervisor supervisor; // Superviseur optionnel

    public Actor(String id) {
        this.id = id;
    }

    public void setSupervisor(Supervisor supervisor) {
        this.supervisor = supervisor;
        if (supervisor != null) {
            supervisor.addSupervisedActor(this);
        }
    }

    // Le coeur du traitement
    public abstract void onReceive(ActorMessage message) throws Exception;

    // Gestion de la tolérance aux pannes
    public void preRestart(Throwable reason) {
        System.out.println("• ⚠️ [SUPERVISION] Redémarrage de l'acteur " + id + " suite à : " + reason.getMessage());
    }

    public void incrementFailureCount() { this.failureCount++; }
    public void resetFailureCount() { this.failureCount = 0; }
}
