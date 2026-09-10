package it.unicam.cs.mpcg.rpg129203.model;

/**
 * Implementa un'abilità magica offensiva. Il suo danno scala in base
 * al livello di chi la utilizza, ignorando l'attacco fisico.
 */
public class FireballSkill implements Skill {
    private static final int MAX_USES = 3;
    private int remainingUses = MAX_USES;

    public String getName() { return "Palla di Fuoco"; }
    public int getRemainingUses() { return remainingUses; }
    public int getMaxUses() { return MAX_USES; }
    public void setRemainingUses(int uses) { this.remainingUses = uses; }
    public void restoreUses() { this.remainingUses = MAX_USES; }

    /**
     * Esegue l'abilità: consuma un uso, calcola i danni scalati per livello
     * e li infligge al bersaglio.
     */
    public String execute(Character user, Character target) {
        if (remainingUses <= 0) return user.getName() + " non ha più mana per la Palla di Fuoco!";
        remainingUses--;
        int damage = 15 + (user.getLevel() * 3);
        target.takeDamage(damage);
        return user.getName() + " lancia Palla di Fuoco per " + damage + " danni! (" + remainingUses + "/" + MAX_USES + " usi)";
    }
}