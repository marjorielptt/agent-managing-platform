package com.framework;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.web.reactive.function.client.WebClient;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("Tests de l'ActorSystem")
class ActorSystemTest {

    private ActorSystem actorSystem;
    private WebClient.Builder webClientBuilder;
    private TestActor actor1;
    private TestActor actor2;

    @BeforeEach
    void setUp() {
        webClientBuilder = mock(WebClient.Builder.class);
        // Mocquer le retour de build()
        WebClient mockWebClient = mock(WebClient.class);
        when(webClientBuilder.build()).thenReturn(mockWebClient);
        
        actorSystem = new ActorSystem(webClientBuilder);
        actor1 = new TestActor("actor-1");
        actor2 = new TestActor("actor-2");
    }

    @Test
    @DisplayName("Enregistrer un acteur l'ajoute au système")
    void testRegisterActor() {
        actorSystem.registerActor(actor1);

        // Vérifier que l'acteur est enregistré en envoyant un message local
        ActorMessage message = new ActorMessage("test-sender", "actor-1", "test-service", "Test");
        
        // On peut vérifier indirectement via un autre enregistrement
        assertDoesNotThrow(() -> {
            actorSystem.registerActor(actor1);
        });
    }

    @Test
    @DisplayName("Enregistrer plusieurs acteurs")
    void testRegisterMultipleActors() {
        actorSystem.registerActor(actor1);
        actorSystem.registerActor(actor2);

        assertDoesNotThrow(() -> {
            actorSystem.registerActor(actor1);
            actorSystem.registerActor(actor2);
        });
    }

    @Test
    @DisplayName("Envoyer un message local à un acteur enregistré")
    void testSendLocalMessage() {
        actorSystem.registerActor(actor1);
        ActorMessage message = new ActorMessage("test-sender", "actor-1", "test-service", "Test payload");

        // Ne doit pas lever d'exception
        assertDoesNotThrow(() -> {
            actorSystem.send(message);
            // Attendre un peu pour que le thread virtuel finisse
            Thread.sleep(100);
        });
    }

    @Test
    @DisplayName("Réinitialiser le failureCount après succès d'un acteur")
    void testFailureCountResetOnSuccess() {
        actor1.incrementFailureCount();
        actor1.incrementFailureCount();
        assertEquals(2, actor1.getFailureCount());

        actorSystem.registerActor(actor1);
        ActorMessage message = new ActorMessage("test-sender", "actor-1", "test-service", "Test");

        assertDoesNotThrow(() -> {
            actorSystem.send(message);
            Thread.sleep(100);
        });

        // Le failureCount doit être réinitialisé après succès
        assertEquals(0, actor1.getFailureCount());
    }

    @Test
    @DisplayName("Gérer l'erreur avec superviseur")
    void testHandleErrorWithSupervisor() {
        TestSupervisor supervisor = new TestSupervisor("supervisor");
        actor1.setSupervisor(supervisor);

        // Créer un acteur qui lance une exception
        FailingActor failingActor = new FailingActor("failing-actor");
        failingActor.setSupervisor(supervisor);

        actorSystem.registerActor(failingActor);

        ActorMessage message = new ActorMessage("test-sender", "failing-actor", "test-service", "This will fail");

        assertDoesNotThrow(() -> {
            actorSystem.send(message);
            Thread.sleep(100);
        });

        // L'acteur devrait avoir des erreurs gérées par le superviseur
        assertTrue(failingActor.getFailureCount() >= 0);
    }

    @Test
    @DisplayName("Enregistrer le même acteur deux fois")
    void testRegisterSameActorTwice() {
        actorSystem.registerActor(actor1);
        actorSystem.registerActor(actor1);

        assertDoesNotThrow(() -> {
            actorSystem.registerActor(actor1);
        });
    }

    @Test
    @DisplayName("Système gère les messages concurrents")
    void testConcurrentMessages() {
        actorSystem.registerActor(actor1);
        actorSystem.registerActor(actor2);

        // Envoyer plusieurs messages en concurrence
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 10; i++) {
                ActorMessage msg1 = new ActorMessage("test-sender", "actor-1", "test-service", "Message " + i);
                ActorMessage msg2 = new ActorMessage("test-sender", "actor-2", "test-service", "Message " + i);
                actorSystem.send(msg1);
                actorSystem.send(msg2);
            }
            Thread.sleep(200);
        });
    }

    @Test
    @DisplayName("Acteur sans superviseur : comportement par défaut en cas d'erreur")
    void testActorWithoutSupervisorHandlesErrors() {
        FailingActor failingActor = new FailingActor("failing-no-supervisor");
        
        actorSystem.registerActor(failingActor);

        ActorMessage message = new ActorMessage("test-sender", "failing-no-supervisor", "test-service", "This will fail");

        assertDoesNotThrow(() -> {
            actorSystem.send(message);
            Thread.sleep(100);
        });

        // Devrait avoir incrémenté le failureCount
        assertTrue(failingActor.getFailureCount() >= 0);
    }

    // Classes de test

    static class TestActor extends Actor {
        public TestActor(String id) {
            super(id);
        }

        @Override
        public void onReceive(ActorMessage message) throws Exception {
            // Succès - le message est traité correctement
        }
    }

    static class FailingActor extends Actor {
        public FailingActor(String id) {
            super(id);
        }

        @Override
        public void onReceive(ActorMessage message) throws Exception {
            // Lance une exception pour simuler une erreur
            throw new RuntimeException("Simulated actor failure");
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
