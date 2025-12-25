package com.framework;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests de la classe Actor")
class ActorTest {

    private TestActor actor;
    private TestSupervisor supervisor;

    @BeforeEach
    void setUp() {
        actor = new TestActor("test-actor");
        supervisor = new TestSupervisor("test-supervisor");
    }

    @Test
    @DisplayName("L'acteur a un ID unique")
    void testActorHasId() {
        assertEquals("test-actor", actor.getId());
    }

    @Test
    @DisplayName("Le failureCount commence à 0")
    void testFailureCountInitialization() {
        assertEquals(0, actor.getFailureCount());
    }

    @Test
    @DisplayName("L'acteur n'a pas de superviseur par défaut")
    void testNoSupervisorByDefault() {
        assertNull(actor.getSupervisor());
    }

    @Test
    @DisplayName("incrementFailureCount augmente le compteur")
    void testIncrementFailureCount() {
        actor.incrementFailureCount();
        assertEquals(1, actor.getFailureCount());

        actor.incrementFailureCount();
        actor.incrementFailureCount();
        assertEquals(3, actor.getFailureCount());
    }

    @Test
    @DisplayName("resetFailureCount remet le compteur à 0")
    void testResetFailureCount() {
        actor.incrementFailureCount();
        actor.incrementFailureCount();
        actor.incrementFailureCount();

        actor.resetFailureCount();
        assertEquals(0, actor.getFailureCount());
    }

    @Test
    @DisplayName("setSupervisor assigne correctement un superviseur")
    void testSetSupervisor() {
        actor.setSupervisor(supervisor);

        assertEquals(supervisor, actor.getSupervisor());
        assertTrue(supervisor.supervisedActors.contains(actor));
    }

    @Test
    @DisplayName("setSupervisor peut être appelé avec null")
    void testSetSupervisorNull() {
        actor.setSupervisor(supervisor);
        actor.setSupervisor(null);

        assertNull(actor.getSupervisor());
    }

    @Test
    @DisplayName("preRestart peut être appelé sans erreur")
    void testPreRestart() {
        Throwable error = new RuntimeException("Test error");
        assertDoesNotThrow(() -> actor.preRestart(error));
    }

    @Test
    @DisplayName("Multiple acteurs peuvent avoir des failureCount indépendants")
    void testMultipleActorsIndependence() {
        TestActor actor2 = new TestActor("test-actor-2");

        actor.incrementFailureCount();
        actor.incrementFailureCount();

        actor2.incrementFailureCount();

        assertEquals(2, actor.getFailureCount());
        assertEquals(1, actor2.getFailureCount());

        actor.resetFailureCount();

        assertEquals(0, actor.getFailureCount());
        assertEquals(1, actor2.getFailureCount());
    }

    @Test
    @DisplayName("Un acteur peut changer de superviseur")
    void testChangeSupervisor() {
        TestSupervisor supervisor2 = new TestSupervisor("supervisor-2");

        actor.setSupervisor(supervisor);
        assertEquals(supervisor, actor.getSupervisor());

        actor.setSupervisor(supervisor2);
        assertEquals(supervisor2, actor.getSupervisor());
    }

    @Test
    @DisplayName("Cycle complet : création, erreur, redémarrage")
    void testCompleteFailureCycle() {
        actor.setSupervisor(supervisor);

        // Simuler 2 erreurs
        actor.incrementFailureCount();
        actor.incrementFailureCount();
        assertEquals(2, actor.getFailureCount());

        // Superviseur les résout en redémarrant
        Supervisor.SupervisionDirective directive = supervisor.handleFailure(actor, new RuntimeException("Error"));
        supervisor.applyDirective(actor, directive, new RuntimeException("Error"));

        // Le failureCount doit être réinitialisé
        assertEquals(0, actor.getFailureCount());
    }

    // Classe test
    static class TestActor extends Actor {
        public TestActor(String id) {
            super(id);
        }

        @Override
        public void onReceive(ActorMessage message) throws Exception {
            // Simple implementation for testing
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
            // Simple implementation for testing
        }
    }
}
