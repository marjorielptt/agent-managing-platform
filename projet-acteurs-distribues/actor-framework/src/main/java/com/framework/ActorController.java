package com.framework;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/actors")
public class ActorController {

    private final ActorSystem actorSystem;

    public ActorController(ActorSystem actorSystem) {
        this.actorSystem = actorSystem;
    }

    @PostMapping("/receive")
    public void receiveMessage(@RequestBody ActorMessage message) {
        // Reçoit un message HTTP et le route en interne
        actorSystem.send(message);
    }
}