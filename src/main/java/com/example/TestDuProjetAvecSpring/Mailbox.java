package com.example.TestDuProjetAvecSpring;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Mailbox {

    private final BlockingQueue<Message> queue = new LinkedBlockingQueue<>();

    public void enqueue(Message message) {
        queue.add(message);
    }

    public Message dequeue() throws InterruptedException {
        return queue.take(); // bloque tant qu’il n’y a pas de message
    }

}
