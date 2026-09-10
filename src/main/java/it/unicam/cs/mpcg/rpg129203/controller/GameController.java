package it.unicam.cs.mpcg.rpg129203.controller;

import it.unicam.cs.mpcg.rpg129203.model.*;
import it.unicam.cs.mpcg.rpg129203.view.GameView;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;

/**
 * Gestisce la logica del combattimento a turni e coordina le interazioni tra
 * il modello (GameEngine) e l'interfaccia (GameView).
 */
public class GameController {
    private GameEngine engine;
    private GameView view;
    private AppController appController;

    /**
     * Costruttore: collega modello e vista, effettua il primo aggiornamento
     * grafico e fa apparire il primo nemico.
     */
    public GameController(GameEngine engine, GameView view, AppController appController) {
        this.engine = engine;
        this.view = view;
        this.appController = appController;
        this.view.setController(this);
        refreshView();
        this.view.updateSkillButtons(engine.getPlayer().getSkills());
        view.appendLog("Un " + engine.getCurrentEnemy().getName() + " ti sbarra la strada!");
    }

    /**
     * Innesca un turno di combattimento basato su un attacco standard.
     */
    public void onAttackPressed() { processTurn(null); }

    /**
     * Innesca un turno di combattimento basato sull'utilizzo di un'abilità specifica.
     */
    public void onSkillPressed(Skill skill) { processTurn(skill); }

    /**
     * Intercetta la richiesta di uscita per dirottarla verso il Game Over
     * o verso il salvataggio standard a seconda dello stato dell'eroe.
     */
    public void onSaveAndQuitPressed() {
        if (engine.getPlayer().isDead()) {
            appController.onGameOver(engine, view);
        } else {
            appController.onSaveAndQuit(engine, view);
        }
    }

    /**
     * Gestisce la logica del turno: applica il danno del giocatore,
     * valuta l'eventuale sconfitta del nemico o, altrimenti, innesca il contrattacco.
     */
    private void processTurn(Skill skillToUse) {
        if (skillToUse != null) {
            if (skillToUse.getRemainingUses() <= 0) {
                view.showMessage("Non hai più utilizzi per questa abilità!");
                return;
            }
            view.appendLog(engine.playerUseSkill(skillToUse));
            view.updateSkillButtons(engine.getPlayer().getSkills());
        } else {
            view.appendLog(engine.playerAttack());
        }

        if (engine.getCurrentEnemy().isDead()) {
            String defeatedEnemyName = engine.getCurrentEnemy().getName();
            int xpGained = engine.resolveEnemyDeath(); // Ripristina anche gli HP a MAX

            view.appendLog("Hai sconfitto " + defeatedEnemyName + "!");
            view.appendLog("Hai ottenuto " + xpGained + " XP.");
            view.appendLog("I tuoi HP sono stati completamente ripristinati!");

            handleVictory(defeatedEnemyName, xpGained);
        } else {
            view.appendLog(engine.enemyAttack());
            if (engine.getPlayer().isDead()) {
                refreshView();
                view.showGameOver();
                return;
            }
            refreshView();
        }
    }

    /**
     * Avvia la sequenza di vittoria, calcolando le abilità disponibili per il level up
     * ed evocando il nemico successivo alla chiusura della finestra di dialogo.
     */
    private void handleVictory(String enemyName, int xpGained) {
        Player p = engine.getPlayer();
        List<Skill> learnableSkills = new ArrayList<>();
        for (Skill s : SkillRegistry.getAllAvailableSkills()) {
            if (!p.hasSkill(s.getName())) {
                learnableSkills.add(s);
            }
        }

        // Mostra la finestra di vittoria ed esegue il callback solo alla pressione del pulsante "Continua"
        view.showVictoryDialog(enemyName, xpGained, p, learnableSkills, () -> {
            if (p.canLevelUp()) {
                // Se c'è un ulteriore level up pendente (es. XP accumulati)
                handleVictory(enemyName, 0);
            } else {
                engine.spawnEnemy();
                view.updateSkillButtons(p.getSkills());
                refreshView();
                view.appendLog("\n--- Un nuovo " + engine.getCurrentEnemy().getName() + " appare! ---");
            }
        });
    }

    /**
     * Sincronizza l'interfaccia grafica con lo stato attuale del modello.
     */
    private void refreshView() {
        view.updateStats(engine.getPlayer(), engine.getCurrentEnemy(), engine.getKillCount());
    }
}