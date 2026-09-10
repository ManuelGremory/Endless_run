package it.unicam.cs.mpcg.rpg129203.model;

public interface SaveSystem {
    void save(SaveData data);
    SaveData load();
}
