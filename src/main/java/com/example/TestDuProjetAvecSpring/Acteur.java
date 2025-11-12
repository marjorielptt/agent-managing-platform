package com.example.TestDuProjetAvecSpring;

public abstract class Acteur implements Runnable {

    private static int compteur = 1;
    private final int id;
    private final Mailbox mailbox;
    private final Thread thread;

    public Acteur() {
        this.id = compteur++;
        this.mailbox = new Mailbox();
        this.thread = new Thread(this, "Acteur-" + id);
        this.thread.start();
    }

    public int getId() {
        return id;
    }

    public void envoyer(Message message) {
        mailbox.enqueue(message);
    }

    @Override
    public void run() {
        try {
            while (true) {
                Message message = mailbox.dequeue();
                onReceive(message);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // La méthode que chaque acteur devra implémenter
    protected abstract void onReceive(Message message);
}