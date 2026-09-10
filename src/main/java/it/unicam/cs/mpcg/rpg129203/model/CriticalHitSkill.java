package it.unicam.cs.mpcg.rpg129203.model;

/**
 * Implementa un'abilità fisica che infligge un danno pari al doppio
 * dell'attacco base dell'utilizzatore.
 */
public class CriticalHitSkill implements Skill {
    private static final int MAX_USES = 4;
    private int remainingUses = MAX_USES;

    public String getName() { return "Colpo Critico"; }
    public int getRemainingUses() { return remainingUses; }
    public int getMaxUses() { return MAX_USES; }
    public void setRemainingUses(int uses) { this.remainingUses = uses; }
    public void restoreUses() { this.remainingUses = MAX_USES; }

    /**
     * Esegue l'abilità: se ci sono usi disponibili, calcola e infligge il danno raddoppiato
     * al bersaglio, restituendo il messaggio di log dell'azione.
     */
    public String execute(Character user, Character target) {
        if (remainingUses <= 0) return user.getName() + " non ha più energia per un Colpo Critico!";
        remainingUses--;
        int damage = user.getAttack() * 2;
        target.takeDamage(damage);
        return user.getName() + " sferra un COLPO CRITICO per " + damage + " danni! (" + remainingUses + "/" + MAX_USES + " usi)";
    }
}