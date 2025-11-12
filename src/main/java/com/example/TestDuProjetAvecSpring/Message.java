package com.example.TestDuProjetAvecSpring;

public class Message {

    private TypeMessage type;
    private String contenu;
    private int emmeteur;
    private int recepteur;

    public Message(TypeMessage type, String contenu, int emmeteur, int recepteur){
        this.type = type;
        this.contenu = contenu;
        this.emmeteur = emmeteur;
        this.recepteur = recepteur;
    }

    public TypeMessage getType() { return type; }

    public String getContenu() {
        return contenu;
    }
    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public int getEmeteur() {
        return emmeteur;
    }
    public void setEmeteur(int emmeteur) {
        this.emmeteur = emmeteur;
    }

    public int getRecepteur() {
        return recepteur;
    }
    public void setRecepteur(int recepteur) {
        this.recepteur = recepteur;
    }
}
