package it.unicam.cs.mpcg.rpg129203.model;

/**
 * Classe astratta base che definisce le entità principali del gioco.
 * Gestisce statistiche comuni come HP, attacco e livello per giocatori e nemici.
 */
public abstract class Character {
    protected String name;
    protected int hp;
    protected int maxHp;
    protected int attack;
    protected int level;

    /**
     * Costruttore: inizializza le statistiche di base del personaggio.
     */
    public Character(String name, int maxHp, int attack, int level) {
        this.name = name;
        this.maxHp = maxHp;
        this.hp = maxHp;
        this.attack = attack;
        this.level = level;
    }

    /**
     * Riduce la salute del personaggio in base al danno subito, senza scendere sotto lo zero.
     */
    public void takeDamage(int amount) { this.hp = Math.max(0, this.hp - amount); }

    /**
     * Ripristina la salute del personaggio senza superare il limite massimo (maxHp).
     */
    public void heal(int amount) { this.hp = Math.min(maxHp, this.hp + amount); }

    /**
     * Ripristina completamente gli HP al valore massimo.
     */
    public void fullHeal() { this.hp = this.maxHp; }

    /**
     * Verifica se il personaggio ha esaurito i punti salute.
     */
    public boolean isDead() { return hp <= 0; }

    public String getName() { return name; }
    public int getHp() { return hp; }
    public int getMaxHp() { return maxHp; }
    public int getAttack() { return attack; }
    public int getLevel() { return level; }
}