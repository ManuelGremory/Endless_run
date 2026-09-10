package it.unicam.cs.mpcg.rpg129203.model;
public interface Skill {
    String getName();
    String execute(Character user, Character target);
    int getRemainingUses();
    int getMaxUses();
    void setRemainingUses(int uses);
    void restoreUses();
}
