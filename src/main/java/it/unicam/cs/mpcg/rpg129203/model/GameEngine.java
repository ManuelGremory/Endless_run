package it.unicam.cs.mpcg.rpg129203.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Il cuore della logica di gioco. Mantiene lo stato della partita,
 * gestisce il giocatore, il nemico corrente e le interazioni di combattimento.
 */
public class GameEngine {
    private Player player;
    private Enemy currentEnemy;
    private int killCount;

    /**
     * Costruttore: inizializza una nuova partita con il nome scelto e genera il primo nemico.
     */
    public GameEngine(String playerName) {
        this.player = new Player(playerName);
        spawnEnemy();
    }

    /**
     * Raccoglie tutti i dati correnti (statistiche, abilità, uccisioni) e li
     * impacchetta in un oggetto SaveData pronto per essere salvato.
     */
    public SaveData exportData() {
        SaveData data = new SaveData();
        data.name = player.getName(); data.level = player.getLevel();
        data.hp = player.getHp(); data.maxHp = player.getMaxHp();
        data.attack = player.getAttack(); data.xp = player.getXp();
        data.xpToNextLevel = player.getXpToNextLevel(); data.kills = this.killCount;

        data.skills = new ArrayList<>();
        for (Skill s : player.getSkills()) {
            data.skills.add(new SaveData.SkillSaveData(s.getName(), s.getRemainingUses()));
        }
        return data;
    }

    /**
     * Ricostruisce lo stato della partita (statistiche ed abilità) a partire da un salvataggio.
     */
    public void restoreData(SaveData data) {
        List<Skill> loadedSkills = new ArrayList<>();
        if (data.skills != null) {
            for (SaveData.SkillSaveData skillData : data.skills) {
                Skill skill = SkillRegistry.getSkill(skillData.name);
                if (skill != null) {
                    skill.setRemainingUses(skillData.remainingUses);
                    loadedSkills.add(skill);
                }
            }
        }
        player.loadStats(data.hp, data.maxHp, data.attack, data.level, data.xp, data.xpToNextLevel, loadedSkills);
        this.killCount = data.kills;
        spawnEnemy();
    }

    /**
     * Genera un nuovo avversario basato sul livello attuale dell'eroe.
     */
    public void spawnEnemy() { this.currentEnemy = new Enemy(player.getLevel()); }

    /**
     * Esegue l'attacco fisico base del giocatore contro il nemico.
     */
    public String playerAttack() {
        int dmg = player.getAttack();
        currentEnemy.takeDamage(dmg);
        return player.getName() + " attacca " + currentEnemy.getName() + " per " + dmg + " danni.";
    }

    /**
     * Delega l'esecuzione di un'abilità specifica verso il bersaglio attuale.
     */
    public String playerUseSkill(Skill skill) {
        return skill.execute(player, currentEnemy);
    }

    /**
     * Permette al nemico di eseguire il suo turno d'attacco contro il giocatore.
     */
    public String enemyAttack() {
        if (currentEnemy.isDead()) return "";
        int dmg = currentEnemy.getAttack();
        player.takeDamage(dmg);
        return currentEnemy.getName() + " attacca per " + dmg + " danni.";
    }

    /**
     * Gestisce le logiche di fine scontro: assegna esperienza, rigenera la salute
     * e aumenta il contatore delle uccisioni.
     */
    public int resolveEnemyDeath() {
        killCount++;
        int xpGained = 10 + (currentEnemy.getLevel() * 5);
        player.addXp(xpGained);
        player.fullHeal(); // Rigenera tutti gli HP persi a fine scontro
        return xpGained;
    }

    public Player getPlayer() { return player; }
    public Enemy getCurrentEnemy() { return currentEnemy; }
    public int getKillCount() { return killCount; }
}