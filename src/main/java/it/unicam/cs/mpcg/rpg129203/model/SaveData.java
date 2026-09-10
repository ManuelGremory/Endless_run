package it.unicam.cs.mpcg.rpg129203.model;
import java.util.List;

/**
 * Struttura Dati Pura (DTO). Raccoglie tutte le variabili che descrivono
 * lo stato da serializzare e deserializzare durante i salvataggi.
 */
public class SaveData {
    public String name;
    public int level;
    public int hp;
    public int maxHp;
    public int attack;
    public int xp;
    public int xpToNextLevel;
    public int kills;
    public int highScore;
    public List<SkillSaveData> skills;

    /**
     * Struttura annidata che definisce lo stato di ogni singola abilità
     * (nome e quanti utilizzi sono rimasti).
     */
    public static class SkillSaveData {
        public String name;
        public int remainingUses;
        public SkillSaveData(String name, int remainingUses) {
            this.name = name; this.remainingUses = remainingUses;
        }
    }
}