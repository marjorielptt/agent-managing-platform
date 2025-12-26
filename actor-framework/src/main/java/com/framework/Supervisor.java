package com.framework;

import java.util.List;
import java.util.ArrayList;

public abstract class Supervisor extends Actor {
    protected List<Actor> supervisedActors = new ArrayList<>();

    public Supervisor(String id) {
        super(id);
    }

    public void addSupervisedActor(Actor actor) {
        supervisedActors.add(actor);
        System.out.println("• Superviseur " + getId() + " surveille maintenant " + actor.getId());
    }

    public void removeSupervisedActor(Actor actor) {
        supervisedActors.remove(actor);
        System.out.println("• Superviseur " + getId() + " ne surveille plus " + actor.getId());
    }

    // Stratégie de supervision
    public abstract SupervisionDirective handleFailure(Actor failedActor, Throwable reason);

    public enum SupervisionDirective {
        RESTART,
        STOP,
        RESUME
    }

    @Override
    public void onReceive(ActorMessage message) throws Exception {
        // Les superviseurs peuvent recevoir des messages de supervision
        if ("SUPERVISION_FAILURE".equals(message.getPayload())) {
            // Traiter un échec signalé
            String failedActorId = (String) message.getPayload(); // payload contient l'ID
            Actor failedActor = supervisedActors.stream()
                .filter(a -> a.getId().equals(failedActorId))
                .findFirst().orElse(null);
            if (failedActor != null) {
                Throwable reason = new RuntimeException("Échec signalé"); 
                SupervisionDirective directive = handleFailure(failedActor, reason);
                applyDirective(failedActor, directive, reason);
            }
        } else {
            handleMessage(message);
        }
    }

    protected void applyDirective(Actor actor, SupervisionDirective directive, Throwable reason) {
        switch (directive) {
            case RESTART:
                actor.preRestart(reason);
                actor.resetFailureCount();
                System.out.println("• Superviseur " + getId() + " redémarre " + actor.getId());
                break;
            case STOP:
                supervisedActors.remove(actor);
                System.out.println("• Superviseur " + getId() + " arrête " + actor.getId());
                break;
            case RESUME:
                actor.resetFailureCount();
                System.out.println("• Superviseur " + getId() + " reprend " + actor.getId());
                break;
        }
    }

    protected abstract void handleMessage(ActorMessage message) throws Exception;
}
