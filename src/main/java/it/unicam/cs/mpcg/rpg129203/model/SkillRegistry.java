package it.unicam.cs.mpcg.rpg129203.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Factory di supporto per la generazione dinamica delle abilità.
 */
public class SkillRegistry {

    /**
     * Ritorna una NUOVA istanza dell'abilità corrispondente al nome passato
     * come parametro (utile soprattutto in fase di caricamento dal salvataggio).
     */
    public static Skill getSkill(String name) {
        return switch (name) {
            case "Cura Magica" -> new HealSkill();
            case "Colpo Critico" -> new CriticalHitSkill();
            case "Palla di Fuoco" -> new FireballSkill();
            default -> null;
        };
    }

    /**
     * Utility per ottenere l'elenco di tutte le abilità disponibili,
     * usata dal Controller per generare scelte randomiche durante il Level Up.
     */
    public static Skill[] getAllAvailableSkills() {
        return new Skill[]{new HealSkill(), new CriticalHitSkill(), new FireballSkill()};
    }
}