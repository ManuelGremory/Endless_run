package it.unicam.cs.mpcg.rpg129203.model;
import java.util.Random;

/**
 * Rappresenta un avversario generato proceduralmente nel gioco.
 */
public class Enemy extends Character {

    /**
     * Costruttore: genera un nemico scalando le sue statistiche (HP e Attacco)
     * in base al livello attuale del giocatore.
     */
    public Enemy(int playerLevel) {
        super(generateName(), 30 + (playerLevel * 15), 5 + (playerLevel * 4), playerLevel);
    }

    /**
     * Seleziona e restituisce casualmente un nome per il nemico da una lista predefinita.
     */
    private static String generateName() {
        String[] names = {"Slime Mutante", "Orco Furioso", "Demone Maggiore", "Golem di Pietra", "Signore dei Lich"};
        return names[new Random().nextInt(names.length)];
    }
}