package it.unicam.cs.mpcg.rpg129203.model;

/**
 * Implementa un'abilità di supporto che permette all'utilizzatore di recuperare HP.
 */
public class HealSkill implements Skill {
    private static final int MAX_USES = 2;
    private int remainingUses = MAX_USES;

    public String getName() { return "Cura Magica"; }
    public int getRemainingUses() { return remainingUses; }
    public int getMaxUses() { return MAX_USES; }
    public void setRemainingUses(int uses) { this.remainingUses = uses; }
    public void restoreUses() { this.remainingUses = MAX_USES; }

    /**
     * Esegue l'abilità: rigenera una quantità di HP basata sul livello dell'utilizzatore,
     * ignorando il parametro "target" in quanto si auto-applica.
     */
    public String execute(Character user, Character target) {
        if (remainingUses <= 0) {
            return user.getName() + " prova a usare Cura Magica ma è esausto!";
        }
        remainingUses--;
        int healAmount = 20 + (user.getLevel() * 5);
        user.heal(healAmount);
        return user.getName() + " si cura di " + healAmount + " HP! (" + remainingUses + "/" + MAX_USES + " usi rimasti)";
    }
}