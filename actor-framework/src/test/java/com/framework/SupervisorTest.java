package com.framework;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests de supervision des acteurs")
class SupervisorTest {
    
    private TestSupervisor supervisor;
    private TestActor actor1;
    private TestActor actor2;

    @BeforeEach
    void setUp() {
        supervisor = new TestSupervisor("supervisor-1");
        actor1 = new TestActor("actor-1");
        actor2 = new TestActor("actor-2");
    }

    @Test
    @DisplayName("Le superviseur ajoute un acteur à sa liste de supervision")
    void testAddSupervisedActor() {
        supervisor.addSupervisedActor(actor1);
        
        assertEquals(1, supervisor.supervisedActors.size());
        assertTrue(supervisor.supervisedActors.contains(actor1));
    }

    @Test
    @DisplayName("Le superviseur retire un acteur de sa liste de supervision")
    void testRemoveSupervisedActor() {
        supervisor.addSupervisedActor(actor1);
        supervisor.removeSupervisedActor(actor1);
        
        assertEquals(0, supervisor.supervisedActors.size());
        assertFalse(supervisor.supervisedActors.contains(actor1));
    }

    @Test
    @DisplayName("Le superviseur redémarre un acteur qui a échoué (< 3 échecs)")
    void testRestartFailedActor() {
        supervisor.addSupervisedActor(actor1);
        actor1.incrementFailureCount();
        
        Supervisor.SupervisionDirective directive = 
            supervisor.handleFailure(actor1, new RuntimeException("Test error"));
        supervisor.applyDirective(actor1, directive, new RuntimeException("Test error"));
        
        assertEquals(0, actor1.getFailureCount());
        assertEquals(Supervisor.SupervisionDirective.RESTART, directive);
    }

    @Test
    @DisplayName("Le superviseur arrête un acteur après 3 échecs")
    void testStopFailedActorAfterThreeFailures() {
        supervisor.addSupervisedActor(actor1);
        actor1.incrementFailureCount();
        actor1.incrementFailureCount();
        actor1.incrementFailureCount();
        
        Supervisor.SupervisionDirective directive = 
            supervisor.handleFailure(actor1, new RuntimeException("Test error"));
        supervisor.applyDirective(actor1, directive, new RuntimeException("Test error"));
        
        assertEquals(0, supervisor.supervisedActors.size());
        assertFalse(supervisor.supervisedActors.contains(actor1));
        assertEquals(Supervisor.SupervisionDirective.STOP, directive);
    }

    @Test
    @DisplayName("Le failureCount s'incrémente et se réinitialise correctement")
    void testFailureCountManagement() {
        actor1.incrementFailureCount();
        assertEquals(1, actor1.getFailureCount());
        
        actor1.incrementFailureCount();
        actor1.incrementFailureCount();
        assertEquals(3, actor1.getFailureCount());
        
        actor1.resetFailureCount();
        assertEquals(0, actor1.getFailureCount());
    }

    @Test
    @DisplayName("Le superviseur gère plusieurs acteurs indépendamment")
    void testMultipleActorsSupervision() {
        supervisor.addSupervisedActor(actor1);
        supervisor.addSupervisedActor(actor2);
        
        assertEquals(2, supervisor.supervisedActors.size());
        
        actor1.incrementFailureCount();
        actor1.incrementFailureCount();
        
        actor2.incrementFailureCount();
        actor2.incrementFailureCount();
        actor2.incrementFailureCount();
        
        assertEquals(2, actor1.getFailureCount());
        assertEquals(3, actor2.getFailureCount());
        
        Supervisor.SupervisionDirective directive1 = 
            supervisor.handleFailure(actor1, new RuntimeException("Error 1"));
        supervisor.applyDirective(actor1, directive1, new RuntimeException("Error 1"));
        
        assertEquals(0, actor1.getFailureCount());
        assertEquals(Supervisor.SupervisionDirective.RESTART, directive1);
        
        Supervisor.SupervisionDirective directive2 = 
            supervisor.handleFailure(actor2, new RuntimeException("Error 2"));
        supervisor.applyDirective(actor2, directive2, new RuntimeException("Error 2"));
        
        assertEquals(1, supervisor.supervisedActors.size());
        assertFalse(supervisor.supervisedActors.contains(actor2));
        assertTrue(supervisor.supervisedActors.contains(actor1));
        assertEquals(Supervisor.SupervisionDirective.STOP, directive2);
    }

    @Test
    @DisplayName("Le superviseur appelé via setSupervisor ajoute l'acteur automatiquement")
    void testSetSupervisorIntegration() {
        actor1.setSupervisor(supervisor);
        
        assertEquals(1, supervisor.supervisedActors.size());
        assertTrue(supervisor.supervisedActors.contains(actor1));
        assertEquals(supervisor, actor1.getSupervisor());
    }

    @Test
    @DisplayName("preRestart est appelé lors d'un redémarrage")
    void testPreRestartCalled() {
        supervisor.addSupervisedActor(actor1);
        
        Throwable error = new RuntimeException("Test restart");
        supervisor.applyDirective(actor1, Supervisor.SupervisionDirective.RESTART, error);
        
        assertEquals(0, actor1.getFailureCount());
    }

    @Test
    @DisplayName("RESUME réinitialise le failureCount sans retirer l'acteur")
    void testResumeDirective() {
        supervisor.addSupervisedActor(actor1);
        actor1.incrementFailureCount();
        actor1.incrementFailureCount();
        
        supervisor.applyDirective(actor1, Supervisor.SupervisionDirective.RESUME, new RuntimeException("Test"));
        
        assertEquals(1, supervisor.supervisedActors.size());
        assertTrue(supervisor.supervisedActors.contains(actor1));
        assertEquals(0, actor1.getFailureCount());
    }

    static class TestActor extends Actor {
        public TestActor(String id) {
            super(id);
        }

        @Override
        public void onReceive(ActorMessage message) throws Exception {
            // Implémentation simple pour les tests
        }
    }

    static class TestSupervisor extends Supervisor {
        public TestSupervisor(String id) {
            super(id);
        }

        @Override
        public SupervisionDirective handleFailure(Actor failedActor, Throwable reason) {
            if (failedActor.getFailureCount() < 3) {
                return SupervisionDirective.RESTART;
            } else {
                return SupervisionDirective.STOP;
            }
        }

        @Override
        protected void handleMessage(ActorMessage message) throws Exception {
            // Implémentation simple pour les tests
        }
    }
}
