package it.unicam.cs.mpcg.rpg129203.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Rappresenta l'eroe controllato dall'utente. Estende le proprietà base aggiungendo
 * la progressione di livello (XP) e l'apprendimento delle abilità.
 */
public class Player extends Character {
    private int xp;
    private int xpToNextLevel;
    private List<Skill> skills;

    /**
     * Costruttore: crea l'eroe con i valori statistici base del Livello 1.
     */
    public Player(String name) {
        super(name, 50, 10, 1);
        this.xp = 0;
        this.xpToNextLevel = 25;
        this.skills = new ArrayList<>();
    }

    /**
     * Metodo utilizzato per sovrascrivere direttamente le statistiche al momento
     * del caricamento di una partita salvata.
     */
    public void loadStats(int hp, int maxHp, int attack, int level, int xp, int xpToNextLevel, List<Skill> skills) {
        this.hp = hp; this.maxHp = maxHp; this.attack = attack;
        this.level = level; this.xp = xp; this.xpToNextLevel = xpToNextLevel;
        this.skills = skills;
    }

    public void addXp(int amount) { this.xp += amount; }
    public boolean canLevelUp() { return xp >= xpToNextLevel; }
    public int getXp() { return xp; }
    public int getXpToNextLevel() { return xpToNextLevel; }
    public List<Skill> getSkills() { return skills; }

    /**
     * Verifica se il giocatore possiede già una determinata abilità per evitarne i duplicati.
     */
    public boolean hasSkill(String skillName) {
        return skills.stream().anyMatch(s -> s.getName().equals(skillName));
    }

    /**
     * Logica condivisa del passaggio di livello: ricalcola l'esperienza necessaria,
     * fornisce una cura parziale e ricarica completamente gli usi delle abilità.
     */
    private void applyLevelUpScaling() {
        this.level++;
        this.xp -= xpToNextLevel;
        this.xpToNextLevel = (int) (xpToNextLevel * 1.5);
        int healAmount = (int)(this.maxHp * 0.3);
        this.heal(healAmount);
        for(Skill s : skills) {
            s.restoreUses();
        }
    }

    /**
     * Effettua il passaggio di livello potenziando solo le statistiche base dell'eroe.
     */
    public void levelUpStats() {
        this.maxHp += 10;
        this.attack += 3;
        applyLevelUpScaling();
    }

    /**
     * Effettua il passaggio di livello apprendendo una nuova abilità al posto
     * delle statistiche base.
     */
    public void learnSkill(Skill skill) {
        skills.add(skill);
        applyLevelUpScaling();
    }
}